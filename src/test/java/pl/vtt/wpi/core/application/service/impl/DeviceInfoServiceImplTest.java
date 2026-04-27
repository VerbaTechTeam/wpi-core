package pl.vtt.wpi.core.application.service.impl;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.DeviceInfoServiceException;
import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceInfoServiceImplTest {

    @Test
    void read_success_returnsDeviceInfo() throws Exception {
        DeviceInfo expected = new DeviceInfo(UUID.randomUUID(), "m", "p", "a", "1", "auth", "mail");
        DeviceInfoServiceImpl service = new DeviceInfoServiceImpl(() -> expected);

        assertEquals(expected, service.read());
    }

    @Test
    void read_portFailure_wrapsException() {
        DeviceInfoServiceImpl service = new DeviceInfoServiceImpl(
                () -> { throw new OutputPortException("x", new IllegalArgumentException("boom")); }
        );

        DeviceInfoServiceException exception = assertThrows(DeviceInfoServiceException.class, service::read);
        assertEquals("Cannot read device info", exception.getMessage());
    }
}
