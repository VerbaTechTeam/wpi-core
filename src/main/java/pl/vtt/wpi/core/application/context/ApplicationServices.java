package pl.vtt.wpi.core.application.context;

import pl.vtt.wpi.core.application.service.AdminPasswordService;
import pl.vtt.wpi.core.application.service.DebugService;
import pl.vtt.wpi.core.application.service.DeviceInfoService;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.application.service.NetworkConfigurationService;
import pl.vtt.wpi.core.application.service.PixelProgramService;
import pl.vtt.wpi.core.application.service.RebootService;
import pl.vtt.wpi.core.application.service.RuntimeDataService;
import pl.vtt.wpi.core.application.service.UserManagementService;

public interface ApplicationServices {
    LoginService loginService();

    AdminPasswordService adminPasswordService();

    UserManagementService userManagementService();

    RuntimeDataService runtimeDataService();

    PixelProgramService pixelProgramService();

    NetworkConfigurationService networkConfigurationService();

    DeviceInfoService deviceInfoService();

    DebugService debugService();

    RebootService rebootService();
}
