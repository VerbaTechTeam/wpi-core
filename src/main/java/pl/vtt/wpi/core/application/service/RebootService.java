package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DeviceRebootException;

public interface RebootService {
    void reboot() throws DeviceRebootException;
}
