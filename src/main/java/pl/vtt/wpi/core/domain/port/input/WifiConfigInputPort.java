package pl.vtt.wpi.core.domain.port.input;

import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class WifiConfigInputPort implements InputPort<WifiConfig> {
    private final RequestFactory<WifiConfig> requestFactory;
    private final RequestSender requestSender;

    public WifiConfigInputPort(
            RequestFactory<WifiConfig> requestFactory,
            RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(WifiConfig obj) throws InputPortException {
        if (obj == null) {
            throw new InputPortException("WiFi config cannot be null");
        }
        if (obj.ssid() == null || obj.password() == null) {
            throw new InputPortException("SSID and password cannot be null");
        }
        if (obj.ssid().isBlank()) {
            throw new InputPortException("SSID cannot be blank");
        }
        try {
            Request<WifiConfig> request = requestFactory.create(Method.PATCH,
                    RequestTarget.WIFI_UPDATE, obj
            );
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot send the WiFi config", e);
        }
    }
}
