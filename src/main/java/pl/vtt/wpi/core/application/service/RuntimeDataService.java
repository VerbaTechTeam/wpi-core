package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.RuntimeDataServiceException;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;

public interface RuntimeDataService {
    RuntimeData read() throws RuntimeDataServiceException;
    void set(RuntimeData runtimeData) throws DataInconsistencyException, RuntimeDataServiceException;
}
