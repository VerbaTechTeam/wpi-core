package pl.vtt.wpi.core.domain.dto;

public record AdminPasswordResetRequest(String secureKey, PasswordDto passwordDto) {
}
