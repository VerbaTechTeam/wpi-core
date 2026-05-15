package pl.vtt.wpi.core.domain.port;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.output.DeviceInfoOutputPort;

import static org.junit.jupiter.api.Assertions.*;

class DeviceInfoOutputPortTest {

    @Test
    void load_readsDeviceInfoFromInfoEndpoint() throws Exception {
        RequestFactory<Void> requestFactory = (_, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, null);
        RequestHandler<Void, DeviceInfo> requestHandler = request -> {
            assertEquals(RequestTarget.INFO.url("http://localhost"), request.url());
            return new DeviceInfo(UUID.randomUUID(), "VTT", "WP", "wpi", "1.0", "VTT", "test@vtt.pl");
        };

        DeviceInfoOutputPort port = new DeviceInfoOutputPort(requestFactory, requestHandler);

        DeviceInfo result = port.load();
        assertEquals("VTT", result.manufacturer());
        assertEquals("WP", result.productName());
        assertEquals("wpi", result.applicationName());
        assertEquals("1.0", result.applicationVersion());
        assertEquals("VTT", result.applicationAuthor());
        assertEquals("test@vtt.pl", result.applicationAuthorEmail());
    }

    @Test
    void load_wrapsException() {
        RequestFactory<Void> requestFactory = (_, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, null);
        RequestHandler<Void, DeviceInfo> requestHandler = _ -> {
            throw new IllegalStateException("boom");
        };

        DeviceInfoOutputPort port = new DeviceInfoOutputPort(requestFactory, requestHandler);

        OutputPortException exception = assertThrows(OutputPortException.class, port::load);
        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Cannot load device info", exception.getMessage());
    }
}
