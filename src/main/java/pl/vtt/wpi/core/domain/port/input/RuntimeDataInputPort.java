package pl.vtt.wpi.core.domain.port.input;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class RuntimeDataInputPort implements InputPort<RuntimeData> {
    private final RequestFactory<RuntimeData> requestFactory;
    private final RequestSender requestSender;

    public RuntimeDataInputPort(
            RequestFactory<RuntimeData> requestFactory,
            RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(RuntimeData obj) throws InputPortException {
        if (obj == null) {
            throw new InputPortException("Runtime data cannot be null");
        }
        boolean hasCoreFields = obj.nol() != null
                && obj.brightness() != null
                && obj.on() != null
                && obj.overflow() != null
                && obj.sensorDependency() != null
                && obj.timeDependency() != null
                && obj.defaultRestartCountdown() != null
                && obj.pixelProgram() != null
                && obj.stepTime() != null;
        boolean hasTimeFields =
                Boolean.FALSE.equals(obj.timeDependency())
                        || (obj.onTime() != null && obj.offTime() != null && obj.zoneId() != null);
        Method method = hasCoreFields && hasTimeFields ? Method.PUT : Method.PATCH;
        try {
            Request<RuntimeData> request = requestFactory.create(
                    obj, method, RequestTarget.DATA_UPDATE
            );
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot send the runtime data", e);
        }
    }
}
