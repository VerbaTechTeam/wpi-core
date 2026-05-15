package pl.vtt.wpi.core.domain.dto;

import pl.vtt.wpi.core.domain.model.User;

public record UserCreateRequest(User user, PasswordDto passwordDto) {
}
