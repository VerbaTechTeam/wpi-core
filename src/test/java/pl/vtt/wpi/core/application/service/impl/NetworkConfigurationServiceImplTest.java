package pl.vtt.wpi.core.application.service.impl;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.domain.model.device.AccessPointConfig;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetworkConfigurationServiceImplTest {

    @Test
    void config_accessPointConfig_isMappedToWifiConfig() {
        AtomicReference<WifiConfig> sent = new AtomicReference<>();
        NetworkConfigurationServiceImpl service = new NetworkConfigurationServiceImpl(sent::set);

        service.config(new AccessPointConfig("ap", "pass"));

        assertEquals(new WifiConfig("ap", "pass"), sent.get());
    }
}
