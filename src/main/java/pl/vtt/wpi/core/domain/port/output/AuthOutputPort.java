package pl.vtt.wpi.core.domain.port.output;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.OutputPort;

public class AuthOutputPort implements OutputPort<Credentials> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, Credentials> requestHandler;

    public AuthOutputPort(RequestFactory<Void> requestFactory,
                          RequestHandler<Void, Credentials> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public Credentials load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(null, Method.POST, RequestTarget.AUTH));
        } catch (Exception e) {
            throw new OutputPortException("Cannot authorize user", e);
        }
    }
}
