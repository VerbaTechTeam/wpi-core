package pl.vtt.wpi.core.application.service.impl;

import java.util.Objects;
import pl.vtt.wpi.core.application.exception.DeviceInfoServiceException;
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
    public DeviceInfo read() throws DeviceInfoServiceException {
        try {
            return deviceInfoOutputPort.load();
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DeviceInfoServiceException("Cannot read device info", cause);
        }
    }
}
