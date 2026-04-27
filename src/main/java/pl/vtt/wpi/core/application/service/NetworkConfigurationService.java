package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.AccessPointConfig;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;
import pl.vtt.wpi.core.application.exception.NetworkConfigurationException;

public interface NetworkConfigurationService {
    void config(WifiConfig config) throws NetworkConfigurationException;
    void config(AccessPointConfig config) throws NetworkConfigurationException;
}
