package pl.vtt.wpi.core.domain;

import pl.vtt.wpi.core.domain.exception.SendingException;
import pl.vtt.wpi.core.domain.model.Request;

public interface RequestSender {
    void send(Request<?> request) throws SendingException;
}
