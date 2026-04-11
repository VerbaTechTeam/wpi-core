package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.domain.port.exception.InputPortException;

public interface InputPort<T> {
    void send(T obj) throws InputPortException;
}
