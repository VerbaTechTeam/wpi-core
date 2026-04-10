package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.AccessPointConfig;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;

public interface NetworkConfigurationService {
    void config(WifiConfig config);
    void config(AccessPointConfig config);
}
