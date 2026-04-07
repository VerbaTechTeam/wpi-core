package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.AccessPointConfig;
import pl.vtt.wpi.core.domain.model.device.WiFiConfig;

public interface NetworkConfigurationService {
    void config(WiFiConfig config);
    void config(AccessPointConfig config);
}
