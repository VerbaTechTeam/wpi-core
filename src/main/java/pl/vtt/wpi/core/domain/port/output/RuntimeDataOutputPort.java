package pl.vtt.wpi.core.domain.port.output;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.OutputPort;

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
