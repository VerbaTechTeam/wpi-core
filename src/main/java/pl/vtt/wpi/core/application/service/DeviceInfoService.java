package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.application.exception.DeviceInfoReadException;

public interface DeviceInfoService {
    DeviceInfo read() throws DeviceInfoReadException;
}
