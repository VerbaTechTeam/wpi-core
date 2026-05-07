package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.application.exception.AdminPasswordResetException;

public interface AdminPasswordService {
    void resetPassword(String secureKey, PasswordDto passwordDto) throws AdminPasswordResetException;
}
