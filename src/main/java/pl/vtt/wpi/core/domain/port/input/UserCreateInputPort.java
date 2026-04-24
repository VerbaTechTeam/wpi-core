package pl.vtt.wpi.core.domain.port.input;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.dto.UserCreateRequest;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.InputPort;

public class UserCreateInputPort implements InputPort<UserCreateRequest> {
    private final RequestFactory<UserCreateRequest> requestFactory;
    private final RequestSender requestSender;

    public UserCreateInputPort(RequestFactory<UserCreateRequest> requestFactory,
                               RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(UserCreateRequest obj) throws InputPortException {
        if (obj == null || obj.user() == null || obj.passwordDto() == null) {
            throw new InputPortException("User and password data cannot be null");
        }
        try {
            Request<UserCreateRequest> request = requestFactory.create(obj, Method.POST, RequestTarget.USERS_CREATE);
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot create user", e);
        }
    }
}
