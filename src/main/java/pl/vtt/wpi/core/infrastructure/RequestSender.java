package pl.vtt.wpi.core.infrastructure;

import pl.vtt.wpi.core.infrastructure.exception.SendingException;

public interface RequestSender {
    void send(Request<?> request) throws SendingException;
}
