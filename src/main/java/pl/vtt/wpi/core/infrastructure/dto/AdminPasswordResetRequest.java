package pl.vtt.wpi.core.infrastructure.dto;

public record AdminPasswordResetRequest(String secureKey, PasswordDto passwordDto) {
}
