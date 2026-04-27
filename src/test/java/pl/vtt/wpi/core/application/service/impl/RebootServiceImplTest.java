package pl.vtt.wpi.core.application.service.impl;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.RebootServiceException;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RebootServiceImplTest {

    @Test
    void reboot_success_sendsNullPayload() throws Exception {
        AtomicBoolean called = new AtomicBoolean(false);
        RebootServiceImpl service = new RebootServiceImpl(_ -> called.set(true));

        service.reboot();

        assertTrue(called.get());
    }

    @Test
    void reboot_portFailure_wrapsException() {
        RebootServiceImpl service = new RebootServiceImpl(
                _ -> { throw new InputPortException("x", new IllegalStateException("boom")); }
        );

        RebootServiceException exception = assertThrows(RebootServiceException.class, service::reboot);
        assertEquals("Cannot reboot device", exception.getMessage());
    }
}
