package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.CurrentState;

import java.util.List;

public interface DebugService {
    List<String> pollLogs();
    List<String> peekLogs();
    CurrentState getCurrentState();
}
