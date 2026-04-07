package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.dto.PasswordDto;

public interface AdminPasswordService {
    void resetPassword(String secureKey, PasswordDto passwordDto);
}
