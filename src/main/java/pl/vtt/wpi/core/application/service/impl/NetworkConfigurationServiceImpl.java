package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
import pl.vtt.wpi.core.application.service.NetworkConfigurationService;
import pl.vtt.wpi.core.domain.model.device.AccessPointConfig;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;

public class NetworkConfigurationServiceImpl implements NetworkConfigurationService {
    private final InputPort<WifiConfig> wifiConfigInputPort;

    public NetworkConfigurationServiceImpl(InputPort<WifiConfig> wifiConfigInputPort) {
        this.wifiConfigInputPort = Objects.requireNonNull(wifiConfigInputPort,
                "wifiConfigInputPort cannot be null");
    }

    @Override
    public void config(WifiConfig config) {
        send(config);
    }

    @Override
    public void config(AccessPointConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Access point config cannot be null");
        }
        send(new WifiConfig(config.ssid(), config.password()));
    }

    private void send(WifiConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("WiFi config cannot be null");
        }
        try {
            wifiConfigInputPort.send(config);
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Cannot update network configuration", cause);
        }
    }
}
