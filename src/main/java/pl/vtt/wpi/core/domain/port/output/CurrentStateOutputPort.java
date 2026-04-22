package pl.vtt.wpi.core.domain.port.output;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.OutputPort;

public class CurrentStateOutputPort implements OutputPort<CurrentState> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, CurrentState> requestHandler;

    public CurrentStateOutputPort(RequestFactory<Void> requestFactory,
                                  RequestHandler<Void, CurrentState> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public CurrentState load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(null, Method.GET, RequestTarget.CURRENT_STATE));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load current state", e);
        }
    }
}
