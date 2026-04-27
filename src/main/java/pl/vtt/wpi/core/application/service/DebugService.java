package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.application.exception.DebugDataAccessException;

import java.util.List;

public interface DebugService {
    List<String> pollLogs() throws DebugDataAccessException;
    List<String> peekLogs() throws DebugDataAccessException;
    CurrentState getCurrentState() throws DebugDataAccessException;
}
