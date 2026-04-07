package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.InputPort;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
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
        Method method = Method.PUT;
        if (obj.nol() == null || obj.brightness() == null || obj.on() == null
                || obj.overflow() == null || obj.sensorDependency() == null
                || obj.timeDependency() == null || obj.defaultRestartCountdown() == null
                || obj.offTime() == null || obj.onTime() == null
                || obj.timeZone() == null || obj.pixelProgram() == null
                || obj.stepTime() == null) {
            method = Method.PATCH;
        }
        Request<RuntimeData> request = requestFactory.create(
                method, RequestTarget.DATA_UPDATE, obj
        );
        try {
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot send the runtime data", e);
        }
    }
}
