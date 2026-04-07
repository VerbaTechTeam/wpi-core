package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;

public interface RuntimeDataService {
    RuntimeData read();
    void set(RuntimeData runtimeData) throws DataInconsistencyException;
    void update(RuntimeData runtimeData) throws DataInconsistencyException;
}
