package pl.vtt.wpi.core.infrastructure.adapter.http;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class LogsDeleteInputPort implements InputPort<Void> {
    private final RequestFactory<Void> requestFactory;
    private final RequestSender requestSender;

    public LogsDeleteInputPort(RequestFactory<Void> requestFactory,
                               RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(Void obj) throws InputPortException {
        try {
            Request<Void> request = requestFactory.create(null, Method.DELETE, RequestTarget.LOGS_DELETE);
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot delete logs", e);
        }
    }
}
