package pl.vtt.wpi.core.domain.model.device;

import java.util.UUID;

public record DeviceInfo(
        UUID uuid,
        String manufacturer,
        String productName,
        String applicationName,
        String applicationVersion,
        String applicationAuthor,
        String applicationAuthorEmail
) {
}
