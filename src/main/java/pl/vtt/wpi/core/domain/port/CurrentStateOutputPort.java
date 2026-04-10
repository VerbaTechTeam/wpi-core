package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

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
            return requestHandler.handle(requestFactory.create(Method.GET, RequestTarget.CURRENT_STATE, null));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load current state", e);
        }
    }
}
