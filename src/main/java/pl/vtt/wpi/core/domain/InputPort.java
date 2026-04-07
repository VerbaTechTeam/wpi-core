package pl.vtt.wpi.core.domain;

import pl.vtt.wpi.core.domain.exception.InputPortException;

public interface InputPort<T> {
    void send(T obj) throws InputPortException;
}
