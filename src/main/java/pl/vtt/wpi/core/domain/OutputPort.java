package pl.vtt.wpi.core.domain;

import pl.vtt.wpi.core.domain.exception.OutputPortException;

public interface OutputPort<T> {
    T load() throws OutputPortException;
}
