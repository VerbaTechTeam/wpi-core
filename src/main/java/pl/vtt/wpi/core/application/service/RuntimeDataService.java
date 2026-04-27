package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.RuntimeDataOperationException;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;

public interface RuntimeDataService {
    RuntimeData read() throws RuntimeDataOperationException;
    void set(RuntimeData runtimeData) throws DataInconsistencyException, RuntimeDataOperationException;
}
