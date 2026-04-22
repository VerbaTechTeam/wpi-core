package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public interface OutputPort<T> {
    T load() throws OutputPortException;
}
