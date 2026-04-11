package pl.vtt.wpi.core.infrastructure;

public interface RequestHandler<T, R> {
    R handle(Request<T> request) throws Exception;
}
