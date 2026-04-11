package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.InputPort;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class UserCreateInputPort implements InputPort<User> {
    private final RequestFactory<User> requestFactory;
    private final RequestSender requestSender;

    public UserCreateInputPort(RequestFactory<User> requestFactory,
                               RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(User obj) throws InputPortException {
        if (obj == null) {
            throw new InputPortException("User cannot be null");
        }
        try {
            Request<User> request = requestFactory.create(Method.POST, RequestTarget.USERS_CREATE, obj);
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot create user", e);
        }
    }
}
