package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.RebootServiceException;

public interface RebootService {
    void reboot() throws RebootServiceException;
}
