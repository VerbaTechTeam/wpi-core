package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
import pl.vtt.wpi.core.application.service.DeviceInfoService;
import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public class DeviceInfoServiceImpl implements DeviceInfoService {
    private final OutputPort<DeviceInfo> deviceInfoOutputPort;

    public DeviceInfoServiceImpl(OutputPort<DeviceInfo> deviceInfoOutputPort) {
        this.deviceInfoOutputPort = Objects.requireNonNull(deviceInfoOutputPort,
                "deviceInfoOutputPort cannot be null");
    }

    @Override
    public DeviceInfo read() {
        try {
            return deviceInfoOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Cannot read device info", cause);
        }
    }
}
