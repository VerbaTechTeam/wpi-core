package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class RuntimeDataOutputPort implements OutputPort<RuntimeData> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, RuntimeData> requestHandler;

    public RuntimeDataOutputPort(RequestFactory<Void> requestFactory,
                                 RequestHandler<Void, RuntimeData> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public RuntimeData load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(Method.GET, RequestTarget.DATA_READ, null));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load runtime data", e);
        }
    }
}
