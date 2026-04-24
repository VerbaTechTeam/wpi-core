package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.service.RuntimeDataService;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public class RuntimeDataServiceImpl implements RuntimeDataService {
    private final OutputPort<RuntimeData> runtimeDataOutputPort;
    private final OutputPort<List<PixelProgram>> pixelProgramsOutputPort;
    private final InputPort<RuntimeData> runtimeDataInputPort;

    public RuntimeDataServiceImpl(OutputPort<RuntimeData> runtimeDataOutputPort,
                                  OutputPort<List<PixelProgram>> pixelProgramsOutputPort,
                                  InputPort<RuntimeData> runtimeDataInputPort) {
        this.runtimeDataOutputPort = Objects.requireNonNull(runtimeDataOutputPort,
                "runtimeDataOutputPort cannot be null");
        this.pixelProgramsOutputPort = Objects.requireNonNull(pixelProgramsOutputPort,
                "pixelProgramsOutputPort cannot be null");
        this.runtimeDataInputPort = Objects.requireNonNull(runtimeDataInputPort,
                "runtimeDataInputPort cannot be null");
    }

    @Override
    public RuntimeData read() {
        try {
            return runtimeDataOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Cannot read runtime data", cause);
        }
    }

    @Override
    public void set(RuntimeData runtimeData) throws DataInconsistencyException {
        if (runtimeData == null) {
            throw new NullPointerException("Runtime data cannot be null");
        }
        validatePixelProgramExists(runtimeData);
        send(runtimeData, "Cannot set runtime data");
    }

    private void send(RuntimeData runtimeData, String message) throws DataInconsistencyException {
        try {
            runtimeDataInputPort.send(runtimeData);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DataInconsistencyException(message, cause);
        }
    }

    private void validatePixelProgramExists(RuntimeData runtimeData) throws DataInconsistencyException {
        if (runtimeData.pixelProgram() == null) {
            return;
        }
        List<PixelProgram> pixelPrograms;
        try {
            pixelPrograms = pixelProgramsOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DataInconsistencyException("Cannot validate runtime data", cause);
        }

        boolean exists = pixelPrograms != null
                && pixelPrograms.stream().anyMatch(program ->
                program != null && program.index() == runtimeData.pixelProgram());

        if (!exists) {
            String message = "Pixel program %d does not exist".formatted(runtimeData.pixelProgram());
            throw new DataInconsistencyException(message, new NoSuchElementException(message));
        }
    }
}
