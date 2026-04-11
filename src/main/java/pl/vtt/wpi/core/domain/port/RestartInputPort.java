package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.InputPort;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class RestartInputPort implements InputPort<Void> {
    private final RequestFactory<Void> requestFactory;
    private final RequestSender requestSender;

    public RestartInputPort(RequestFactory<Void> requestFactory,
                            RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(Void obj) throws InputPortException {
        try {
            Request<Void> request = requestFactory.create(Method.POST, RequestTarget.RESTART, null);
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot restart device", e);
        }
    }
}
