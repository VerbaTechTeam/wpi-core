package pl.vtt.wpi.core.application.service.impl;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.AdminPasswordResetException;
import pl.vtt.wpi.core.domain.dto.AdminPasswordResetRequest;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdminPasswordServiceImplTest {

    @Test
    void resetPassword_validData_sendsRequest() throws Exception {
        AtomicReference<AdminPasswordResetRequest> sent = new AtomicReference<>();
        InputPort<AdminPasswordResetRequest> inputPort = sent::set;
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(inputPort);

        PasswordDto passwordDto = new PasswordDto("secret", "secret");
        service.resetPassword("secure-key", passwordDto);

        assertEquals("secure-key", sent.get().secureKey());
        assertEquals(passwordDto, sent.get().passwordDto());
    }

    @Test
    void resetPassword_blankPassword_throwsAdminPasswordResetException() {
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(_ -> {});

        assertThrows(AdminPasswordResetException.class,
                () -> service.resetPassword("secure-key", new PasswordDto(" ", " ")));
    }

    @Test
    void resetPassword_inputPortExceptionWithoutCause_isTranslated() {
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(
                _ -> { throw new InputPortException("port failure"); }
        );

        AdminPasswordResetException exception = assertThrows(AdminPasswordResetException.class,
                () -> service.resetPassword("secure-key", new PasswordDto("secret", "secret")));

        assertEquals("Cannot reset admin password", exception.getMessage());
        assertEquals("port failure", exception.getCause().getMessage());
    }

    @Test
    void resetPassword_inputPortExceptionWithCause_isTranslated() {
        RuntimeException cause = new RuntimeException("cause");
        AdminPasswordServiceImpl service = new AdminPasswordServiceImpl(
                _ -> { throw new InputPortException("port failure", cause); }
        );

        AdminPasswordResetException exception = assertThrows(AdminPasswordResetException.class,
                () -> service.resetPassword("secure-key", new PasswordDto("secret", "secret")));

        assertEquals("Cannot reset admin password", exception.getMessage());
        assertEquals("cause", exception.getCause().getMessage());
    }
}
