package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
import pl.vtt.wpi.core.application.exception.DeviceRebootException;
import pl.vtt.wpi.core.application.service.RebootService;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;

public class RebootServiceImpl implements RebootService {
    private final InputPort<Void> rebootInputPort;

    public RebootServiceImpl(InputPort<Void> rebootInputPort) {
        this.rebootInputPort = Objects.requireNonNull(rebootInputPort,
                "rebootInputPort cannot be null");
    }

    @Override
    public void reboot() throws DeviceRebootException {
        try {
            rebootInputPort.send(null);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DeviceRebootException("Cannot reboot device", cause);
        }
    }
}
