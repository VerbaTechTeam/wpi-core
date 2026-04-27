package pl.vtt.wpi.core.application.service.impl;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.AdminPasswordServiceException;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.port.InputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdminPasswordServiceImplTest {

    @Test
    void resetPassword_validData_sendsRequest() throws Exception {
        AtomicReference<AdminPasswordServiceImpl.AdminPasswordResetRequest> sent = new AtomicReference<>();
        InputPort<AdminPasswordServiceImpl.AdminPasswordResetRequest> inputPort = sent::set;
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(inputPort);

        PasswordDto passwordDto = new PasswordDto("secret", "secret");
        service.resetPassword("secure-key", passwordDto);

        assertEquals("secure-key", sent.get().secureKey());
        assertEquals(passwordDto, sent.get().passwordDto());
    }

    @Test
    void resetPassword_blankPassword_throwsAdminPasswordServiceException() {
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(_ -> {});

        assertThrows(AdminPasswordServiceException.class,
                () -> service.resetPassword("secure-key", new PasswordDto(" ", " ")));
    }
}
