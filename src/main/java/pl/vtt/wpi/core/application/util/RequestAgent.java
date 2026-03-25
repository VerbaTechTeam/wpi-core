package pl.vtt.wpi.core.application.util;

import pl.vtt.wpi.core.domain.model.Request;

public interface RequestAgent<T> {
    ResponseProxy send(Request<T> request) throws Exception;
}
