package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

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
            return requestHandler.handle(requestFactory.create(Method.POST, RequestTarget.AUTH, null));
        } catch (Exception e) {
            throw new OutputPortException("Cannot authorize user", e);
        }
    }
}
