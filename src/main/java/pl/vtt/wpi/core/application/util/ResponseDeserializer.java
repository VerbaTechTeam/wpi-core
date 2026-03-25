package pl.vtt.wpi.core.application.util;

public interface ResponseDeserializer {
    <R> R deserialize(String responseBody, Class<R> type) throws Exception;
}
