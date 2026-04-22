package pl.vtt.wpi.core.domain.port;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.input.WifiConfigInputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WifiConfigInputPortTest {
    @Test
    void send_usesPatchMethod() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<WifiConfig> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;
        WifiConfigInputPort port = new WifiConfigInputPort(requestFactory, requestSender);

        port.send(new WifiConfig("ssid", "pass"));

        assertEquals(Method.PATCH, sent.get().method());
    }

    @Test
    void send_throwsWhenSsidOrPasswordMissing() {
        WifiConfigInputPort port = new WifiConfigInputPort((_, _, _, _) -> null, _ -> {});

        assertThrows(InputPortException.class, () -> port.send(new WifiConfig(null, "pass")));
        assertThrows(InputPortException.class, () -> port.send(new WifiConfig("ssid", null)));
    }
}
