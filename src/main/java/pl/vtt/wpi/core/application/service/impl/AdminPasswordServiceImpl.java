package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
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
    public void resetPassword(String secureKey, PasswordDto passwordDto) {
        if (secureKey == null || secureKey.isBlank()) {
            throw new IllegalArgumentException("Secure key cannot be null or blank");
        }
        if (passwordDto == null || passwordDto.password() == null || passwordDto.passwordConfirmation() == null) {
            throw new IllegalArgumentException("Password data cannot be null");
        }
        if (!passwordDto.password().equals(passwordDto.passwordConfirmation())) {
            throw new IllegalArgumentException("Password and password confirmation do not match");
        }
        try {
            adminPasswordResetInputPort.send(new AdminPasswordResetRequest(secureKey, passwordDto));
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Cannot reset admin password", cause);
        }
    }

    public record AdminPasswordResetRequest(String secureKey, PasswordDto passwordDto) {
    }
}
