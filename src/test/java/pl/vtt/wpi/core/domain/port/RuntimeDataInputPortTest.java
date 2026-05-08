package pl.vtt.wpi.core.domain.port;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.infrastructure.adapter.http.RuntimeDataInputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeDataInputPortTest {
    @Test
    void send_usesPutForFullPayload() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<RuntimeData> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;
        RuntimeDataInputPort port = new RuntimeDataInputPort(requestFactory, requestSender);

        RuntimeData data = new RuntimeData(1, 0, 1.0, 100, 5, false, true,
                true, true, LocalTime.NOON, LocalTime.MIDNIGHT, ZoneId.of("UTC"));
        port.send(data);

        assertEquals(Method.PUT, sent.get().method());
    }
}
