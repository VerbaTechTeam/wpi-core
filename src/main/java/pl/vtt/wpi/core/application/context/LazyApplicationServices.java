package pl.vtt.wpi.core.application.context;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import pl.vtt.wpi.core.application.service.AdminPasswordService;
import pl.vtt.wpi.core.application.service.DebugService;
import pl.vtt.wpi.core.application.service.DeviceInfoService;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.application.service.NetworkConfigurationService;
import pl.vtt.wpi.core.application.service.PixelProgramService;
import pl.vtt.wpi.core.application.service.RebootService;
import pl.vtt.wpi.core.application.service.RuntimeDataService;
import pl.vtt.wpi.core.application.service.UserManagementService;
import pl.vtt.wpi.core.application.service.impl.AdminPasswordServiceImpl;
import pl.vtt.wpi.core.application.service.impl.DebugServiceImpl;
import pl.vtt.wpi.core.application.service.impl.DeviceInfoServiceImpl;
import pl.vtt.wpi.core.application.service.impl.LoginServiceImpl;
import pl.vtt.wpi.core.application.service.impl.NetworkConfigurationServiceImpl;
import pl.vtt.wpi.core.application.service.impl.PixelProgramServiceImpl;
import pl.vtt.wpi.core.application.service.impl.RebootServiceImpl;
import pl.vtt.wpi.core.application.service.impl.RuntimeDataServiceImpl;
import pl.vtt.wpi.core.application.service.impl.UserManagementServiceImpl;
import pl.vtt.wpi.core.domain.dto.AdminPasswordResetRequest;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.dto.UserCreateRequest;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.model.device.DeviceInfo;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.device.WifiConfig;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;

public final class LazyApplicationServices implements ApplicationServices {
    private final Lazy<OutputPort<Credentials>> authOutputPort;
    private final Lazy<InputPort<AdminPasswordResetRequest>> adminPasswordResetInputPort;
    private final Lazy<OutputPort<List<User>>> usersOutputPort;
    private final Lazy<InputPort<UserCreateRequest>> userCreateRequestInputPort;
    private final Lazy<InputPort<PasswordDto>> changePasswordInputPort;
    private final Lazy<InputPort<User>> removeUserInputPort;
    private final Lazy<OutputPort<RuntimeData>> runtimeDataOutputPort;
    private final Lazy<OutputPort<List<PixelProgram>>> pixelProgramsOutputPort;
    private final Lazy<InputPort<RuntimeData>> runtimeDataInputPort;
    private final Lazy<InputPort<List<PixelProgram>>> pixelProgramsInputPort;
    private final Lazy<InputPort<WifiConfig>> wifiConfigInputPort;
    private final Lazy<OutputPort<DeviceInfo>> deviceInfoOutputPort;
    private final Lazy<OutputPort<List<String>>> logsOutputPort;
    private final Lazy<InputPort<Void>> logsDeleteInputPort;
    private final Lazy<OutputPort<CurrentState>> currentStateOutputPort;
    private final Lazy<InputPort<Void>> rebootInputPort;

    private final Lazy<LoginService> loginService;
    private final Lazy<AdminPasswordService> adminPasswordService;
    private final Lazy<UserManagementService> userManagementService;
    private final Lazy<RuntimeDataService> runtimeDataService;
    private final Lazy<PixelProgramService> pixelProgramService;
    private final Lazy<NetworkConfigurationService> networkConfigurationService;
    private final Lazy<DeviceInfoService> deviceInfoService;
    private final Lazy<DebugService> debugService;
    private final Lazy<RebootService> rebootService;

    private LazyApplicationServices(Builder builder) {
        this.authOutputPort = lazy(builder.authOutputPort, "authOutputPort");
        this.adminPasswordResetInputPort = lazy(builder.adminPasswordResetInputPort,
                "adminPasswordResetInputPort");
        this.usersOutputPort = lazy(builder.usersOutputPort, "usersOutputPort");
        this.userCreateRequestInputPort = lazy(builder.userCreateRequestInputPort,
                "userCreateRequestInputPort");
        this.changePasswordInputPort = lazy(builder.changePasswordInputPort, "changePasswordInputPort");
        this.removeUserInputPort = lazy(builder.removeUserInputPort, "removeUserInputPort");
        this.runtimeDataOutputPort = lazy(builder.runtimeDataOutputPort, "runtimeDataOutputPort");
        this.pixelProgramsOutputPort = lazy(builder.pixelProgramsOutputPort, "pixelProgramsOutputPort");
        this.runtimeDataInputPort = lazy(builder.runtimeDataInputPort, "runtimeDataInputPort");
        this.pixelProgramsInputPort = lazy(builder.pixelProgramsInputPort, "pixelProgramsInputPort");
        this.wifiConfigInputPort = lazy(builder.wifiConfigInputPort, "wifiConfigInputPort");
        this.deviceInfoOutputPort = lazy(builder.deviceInfoOutputPort, "deviceInfoOutputPort");
        this.logsOutputPort = lazy(builder.logsOutputPort, "logsOutputPort");
        this.logsDeleteInputPort = lazy(builder.logsDeleteInputPort, "logsDeleteInputPort");
        this.currentStateOutputPort = lazy(builder.currentStateOutputPort, "currentStateOutputPort");
        this.rebootInputPort = lazy(builder.rebootInputPort, "rebootInputPort");

        this.loginService = Lazy.of(() -> new LoginServiceImpl(authOutputPort.get()));
        this.adminPasswordService = Lazy.of(() -> new AdminPasswordServiceImpl(adminPasswordResetInputPort.get()));
        this.userManagementService = Lazy.of(() -> new UserManagementServiceImpl(usersOutputPort.get(),
                userCreateRequestInputPort.get(), changePasswordInputPort.get(), removeUserInputPort.get()));
        this.runtimeDataService = Lazy.of(() -> new RuntimeDataServiceImpl(runtimeDataOutputPort.get(),
                pixelProgramsOutputPort.get(), runtimeDataInputPort.get()));
        this.pixelProgramService = Lazy.of(() -> new PixelProgramServiceImpl(pixelProgramsOutputPort.get(),
                pixelProgramsInputPort.get()));
        this.networkConfigurationService = Lazy.of(() -> new NetworkConfigurationServiceImpl(wifiConfigInputPort.get()));
        this.deviceInfoService = Lazy.of(() -> new DeviceInfoServiceImpl(deviceInfoOutputPort.get()));
        this.debugService = Lazy.of(() -> new DebugServiceImpl(logsOutputPort.get(), logsDeleteInputPort.get(),
                currentStateOutputPort.get()));
        this.rebootService = Lazy.of(() -> new RebootServiceImpl(rebootInputPort.get()));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public LoginService loginService() {
        return loginService.get();
    }

    @Override
    public AdminPasswordService adminPasswordService() {
        return adminPasswordService.get();
    }

    @Override
    public UserManagementService userManagementService() {
        return userManagementService.get();
    }

    @Override
    public RuntimeDataService runtimeDataService() {
        return runtimeDataService.get();
    }

    @Override
    public PixelProgramService pixelProgramService() {
        return pixelProgramService.get();
    }

    @Override
    public NetworkConfigurationService networkConfigurationService() {
        return networkConfigurationService.get();
    }

    @Override
    public DeviceInfoService deviceInfoService() {
        return deviceInfoService.get();
    }

    @Override
    public DebugService debugService() {
        return debugService.get();
    }

    @Override
    public RebootService rebootService() {
        return rebootService.get();
    }

    private static <T> Lazy<T> lazy(Supplier<? extends T> supplier, String name) {
        return Lazy.of(Objects.requireNonNull(supplier, name + " supplier cannot be null"));
    }

    public static final class Builder {
        private Supplier<? extends OutputPort<Credentials>> authOutputPort;
        private Supplier<? extends InputPort<AdminPasswordResetRequest>> adminPasswordResetInputPort;
        private Supplier<? extends OutputPort<List<User>>> usersOutputPort;
        private Supplier<? extends InputPort<UserCreateRequest>> userCreateRequestInputPort;
        private Supplier<? extends InputPort<PasswordDto>> changePasswordInputPort;
        private Supplier<? extends InputPort<User>> removeUserInputPort;
        private Supplier<? extends OutputPort<RuntimeData>> runtimeDataOutputPort;
        private Supplier<? extends OutputPort<List<PixelProgram>>> pixelProgramsOutputPort;
        private Supplier<? extends InputPort<RuntimeData>> runtimeDataInputPort;
        private Supplier<? extends InputPort<List<PixelProgram>>> pixelProgramsInputPort;
        private Supplier<? extends InputPort<WifiConfig>> wifiConfigInputPort;
        private Supplier<? extends OutputPort<DeviceInfo>> deviceInfoOutputPort;
        private Supplier<? extends OutputPort<List<String>>> logsOutputPort;
        private Supplier<? extends InputPort<Void>> logsDeleteInputPort;
        private Supplier<? extends OutputPort<CurrentState>> currentStateOutputPort;
        private Supplier<? extends InputPort<Void>> rebootInputPort;

        public Builder authOutputPort(Supplier<? extends OutputPort<Credentials>> authOutputPort) {
            this.authOutputPort = authOutputPort;
            return this;
        }

        public Builder adminPasswordResetInputPort(
                Supplier<? extends InputPort<AdminPasswordResetRequest>> adminPasswordResetInputPort) {
            this.adminPasswordResetInputPort = adminPasswordResetInputPort;
            return this;
        }

        public Builder usersOutputPort(Supplier<? extends OutputPort<List<User>>> usersOutputPort) {
            this.usersOutputPort = usersOutputPort;
            return this;
        }

        public Builder userCreateRequestInputPort(
                Supplier<? extends InputPort<UserCreateRequest>> userCreateRequestInputPort) {
            this.userCreateRequestInputPort = userCreateRequestInputPort;
            return this;
        }

        public Builder changePasswordInputPort(Supplier<? extends InputPort<PasswordDto>> changePasswordInputPort) {
            this.changePasswordInputPort = changePasswordInputPort;
            return this;
        }

        public Builder removeUserInputPort(Supplier<? extends InputPort<User>> removeUserInputPort) {
            this.removeUserInputPort = removeUserInputPort;
            return this;
        }

        public Builder runtimeDataOutputPort(Supplier<? extends OutputPort<RuntimeData>> runtimeDataOutputPort) {
            this.runtimeDataOutputPort = runtimeDataOutputPort;
            return this;
        }

        public Builder pixelProgramsOutputPort(
                Supplier<? extends OutputPort<List<PixelProgram>>> pixelProgramsOutputPort) {
            this.pixelProgramsOutputPort = pixelProgramsOutputPort;
            return this;
        }

        public Builder runtimeDataInputPort(Supplier<? extends InputPort<RuntimeData>> runtimeDataInputPort) {
            this.runtimeDataInputPort = runtimeDataInputPort;
            return this;
        }

        public Builder pixelProgramsInputPort(
                Supplier<? extends InputPort<List<PixelProgram>>> pixelProgramsInputPort) {
            this.pixelProgramsInputPort = pixelProgramsInputPort;
            return this;
        }

        public Builder wifiConfigInputPort(Supplier<? extends InputPort<WifiConfig>> wifiConfigInputPort) {
            this.wifiConfigInputPort = wifiConfigInputPort;
            return this;
        }

        public Builder deviceInfoOutputPort(Supplier<? extends OutputPort<DeviceInfo>> deviceInfoOutputPort) {
            this.deviceInfoOutputPort = deviceInfoOutputPort;
            return this;
        }

        public Builder logsOutputPort(Supplier<? extends OutputPort<List<String>>> logsOutputPort) {
            this.logsOutputPort = logsOutputPort;
            return this;
        }

        public Builder logsDeleteInputPort(Supplier<? extends InputPort<Void>> logsDeleteInputPort) {
            this.logsDeleteInputPort = logsDeleteInputPort;
            return this;
        }

        public Builder currentStateOutputPort(Supplier<? extends OutputPort<CurrentState>> currentStateOutputPort) {
            this.currentStateOutputPort = currentStateOutputPort;
            return this;
        }

        public Builder rebootInputPort(Supplier<? extends InputPort<Void>> rebootInputPort) {
            this.rebootInputPort = rebootInputPort;
            return this;
        }

        public ApplicationServices build() {
            return new LazyApplicationServices(this);
        }
    }
}
