package pl.vtt.wpi.core.infrastructure.dto;

import pl.vtt.wpi.core.domain.model.User;

public record UserCreateRequest(User user, PasswordDto passwordDto) {
}
