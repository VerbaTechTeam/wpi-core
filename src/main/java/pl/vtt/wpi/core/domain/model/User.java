package pl.vtt.wpi.core.domain.model;

import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import java.util.Set;

public record User(String username, Set<UserGroup> groups) {
}
