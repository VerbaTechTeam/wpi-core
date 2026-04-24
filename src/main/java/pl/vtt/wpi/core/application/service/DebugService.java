package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.CurrentState;

import java.util.List;

public interface DebugService {
    /**
     * Best-effort non-atomic read-and-clear operation.
     * Reading logs and clearing logs happen in separate calls and may race with concurrent log writes.
     */
    List<String> pollLogs();
    List<String> peekLogs();
    CurrentState getCurrentState();
}
