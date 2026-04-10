package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class DeviceInfoOutputPort implements OutputPort<DeviceInfo> {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, DeviceInfo> requestHandler;

    public DeviceInfoOutputPort(RequestFactory<Void> requestFactory,
                                RequestHandler<Void, DeviceInfo> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public DeviceInfo load() throws OutputPortException {
        try {
            return requestHandler.handle(requestFactory.create(Method.GET, RequestTarget.INFO, null));
        } catch (Exception e) {
            throw new OutputPortException("Cannot load device info", e);
        }
    }
}
