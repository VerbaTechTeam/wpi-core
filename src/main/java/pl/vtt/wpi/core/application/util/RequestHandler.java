package pl.vtt.wpi.core.application.util;

import pl.vtt.wpi.core.domain.model.Request;

public interface RequestHandler<T, R> {
    R handle(Request<T> request) throws Exception;
}
