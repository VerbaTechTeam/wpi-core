package pl.vtt.wpi.core.domain.port;

import java.util.List;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

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
            throw new OutputPortException("Cannot load users", e);
        }
    }
}
