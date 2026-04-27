package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.Objects;
import pl.vtt.wpi.core.application.exception.DebugServiceException;
import pl.vtt.wpi.core.application.service.DebugService;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public class DebugServiceImpl implements DebugService {
    private final OutputPort<List<String>> logsOutputPort;
    private final InputPort<Void> logsDeleteInputPort;
    private final OutputPort<CurrentState> currentStateOutputPort;

    public DebugServiceImpl(OutputPort<List<String>> logsOutputPort,
                            InputPort<Void> logsDeleteInputPort,
                            OutputPort<CurrentState> currentStateOutputPort) {
        this.logsOutputPort = Objects.requireNonNull(logsOutputPort, "logsOutputPort cannot be null");
        this.logsDeleteInputPort = Objects.requireNonNull(logsDeleteInputPort,
                "logsDeleteInputPort cannot be null");
        this.currentStateOutputPort = Objects.requireNonNull(currentStateOutputPort,
                "currentStateOutputPort cannot be null");
    }

    /**
     * Best-effort non-atomic read-and-clear operation.
     * <p>
     * This method performs {@link #peekLogs()} and then issues a separate delete call.
     * Log entries appended between these calls may be deleted without appearing in the returned list.
     * </p>
     */
    @Override
    public List<String> pollLogs() throws DebugServiceException {
        List<String> logs = peekLogs();
        try {
            logsDeleteInputPort.send(null);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DebugServiceException("Cannot clear logs", cause);
        }
        return logs;
    }

    @Override
    public List<String> peekLogs() throws DebugServiceException {
        try {
            return logsOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DebugServiceException("Cannot read logs", cause);
        }
    }

    @Override
    public CurrentState getCurrentState() throws DebugServiceException {
        try {
            return currentStateOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DebugServiceException("Cannot read current state", cause);
        }
    }
}
