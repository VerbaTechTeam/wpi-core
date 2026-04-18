package pl.vtt.wpi.core.domain.port.output;

import java.util.List;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.OutputPort;

public class UsersOutputPort implements OutputPort<List<User>> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, List<User>> requestHandler;

    public UsersOutputPort(RequestFactory<Void> requestFactory,
                           RequestHandler<Void, List<User>> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public List<User> load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(Method.GET, RequestTarget.USERS_READ, null));
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new OutputPortException("Cannot load users", e);
        }
    }
}
