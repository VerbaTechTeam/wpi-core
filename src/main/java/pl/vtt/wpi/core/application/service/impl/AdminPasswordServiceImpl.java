package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
import pl.vtt.wpi.core.application.exception.AdminPasswordResetException;
import pl.vtt.wpi.core.application.service.AdminPasswordService;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;

public class AdminPasswordServiceImpl implements AdminPasswordService {
    private final InputPort<AdminPasswordResetRequest> adminPasswordResetInputPort;

    public AdminPasswordServiceImpl(InputPort<AdminPasswordResetRequest> adminPasswordResetInputPort) {
        this.adminPasswordResetInputPort = Objects.requireNonNull(adminPasswordResetInputPort,
                "adminPasswordResetInputPort cannot be null");
    }

    @Override
    public void resetPassword(String secureKey, PasswordDto passwordDto) throws AdminPasswordResetException {
        if (secureKey == null || secureKey.isBlank()) {
            throw new AdminPasswordResetException("Secure key cannot be null or blank");
        }
        if (passwordDto == null || passwordDto.password() == null || passwordDto.passwordConfirmation() == null) {
            throw new AdminPasswordResetException("Password data cannot be null");
        }
        if (passwordDto.password().isBlank() || passwordDto.passwordConfirmation().isBlank()) {
            throw new AdminPasswordResetException("Password and password confirmation cannot be blank");
        }
        if (!passwordDto.password().equals(passwordDto.passwordConfirmation())) {
            throw new AdminPasswordResetException("Password and password confirmation do not match");
        }
        try {
            adminPasswordResetInputPort.send(new AdminPasswordResetRequest(secureKey, passwordDto));
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new AdminPasswordResetException("Cannot reset admin password", cause);
        }
    }

    public record AdminPasswordResetRequest(String secureKey, PasswordDto passwordDto) {
    }
}
