package pl.vtt.wpi.core.domain.port;

import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.InputPort;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.device.WiFiConfig;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public class WifiConfigInputPort implements InputPort<WiFiConfig> {
    private final RequestFactory<WiFiConfig> requestFactory;
    private final RequestSender requestSender;

    public WifiConfigInputPort(
            RequestFactory<WiFiConfig> requestFactory,
            RequestSender requestSender) {
        this.requestFactory = requestFactory;
        this.requestSender = requestSender;
    }

    @Override
    public void send(WiFiConfig obj) throws InputPortException {
        if (obj == null) {
            throw new InputPortException("WiFi config cannot be null");
        }
        if (obj.ssid() == null || obj.password() == null) {
            throw new InputPortException("SSID and password cannot be null");
        }
        Request<WiFiConfig> request = requestFactory.create(Method.PATCH,
                RequestTarget.WIFI_UPDATE, obj
        );
        try {
            requestSender.send(request);
        } catch (Exception e) {
            throw new InputPortException("Cannot send the WiFi config", e);
        }
    }
}
