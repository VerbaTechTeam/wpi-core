package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.application.exception.DebugDataAccessException;

import java.util.List;

public interface DebugService {
    /**
     * Best-effort non-atomic read-and-clear operation.
     * Reading logs and clearing logs happen in separate calls and may race with concurrent log writes.
     */
    List<String> pollLogs() throws DebugDataAccessException;
    List<String> peekLogs() throws DebugDataAccessException;
    CurrentState getCurrentState() throws DebugDataAccessException;
}
