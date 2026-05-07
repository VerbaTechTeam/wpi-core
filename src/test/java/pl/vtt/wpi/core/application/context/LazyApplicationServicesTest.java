package pl.vtt.wpi.core.application.context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LazyApplicationServicesTest {

    @Test
    @DisplayName("Creates services and dependencies lazily")
    void creates_services_and_dependencies_lazily() {
        AtomicInteger authPortCreations = new AtomicInteger();
        ApplicationServices services = baseBuilder()
                .authOutputPort(() -> {
                    authPortCreations.incrementAndGet();
                    return outputPort(new Credentials("admin", "token"));
                })
                .build();

        assertEquals(0, authPortCreations.get());

        assertSame(services.loginService(), services.loginService());
        assertEquals(1, authPortCreations.get());
    }

    @Test
    @DisplayName("Does not resolve unrelated dependencies")
    void does_not_resolve_unrelated_dependencies() {
        AtomicInteger runtimeDataPortCreations = new AtomicInteger();
        ApplicationServices services = baseBuilder()
                .runtimeDataOutputPort(() -> {
                    runtimeDataPortCreations.incrementAndGet();
                    return outputPort(null);
                })
                .build();

        assertNotNull(services.loginService());

        assertEquals(0, runtimeDataPortCreations.get());
    }

    @Test
    @DisplayName("Fails fast when a required supplier is missing")
    void fails_fast_when_required_supplier_is_missing() {
        LazyApplicationServices.Builder builder = baseBuilder().authOutputPort(null);

        assertThrows(NullPointerException.class, builder::build);
    }

    private static LazyApplicationServices.Builder baseBuilder() {
        return LazyApplicationServices.builder()
                .authOutputPort(() -> outputPort(new Credentials("admin", "token")))
                .adminPasswordResetInputPort(() -> inputPort())
                .usersOutputPort(() -> outputPort(List.<User>of()))
                .userCreateRequestInputPort(() -> inputPort())
                .changePasswordInputPort(() -> inputPort())
                .removeUserInputPort(() -> inputPort())
                .runtimeDataOutputPort(() -> outputPort(null))
                .pixelProgramsOutputPort(() -> outputPort(List.<PixelProgram>of()))
                .runtimeDataInputPort(() -> inputPort())
                .pixelProgramsInputPort(() -> inputPort())
                .wifiConfigInputPort(() -> inputPort())
                .deviceInfoOutputPort(() -> outputPort(null))
                .logsOutputPort(() -> outputPort(List.<String>of()))
                .logsDeleteInputPort(() -> inputPort())
                .currentStateOutputPort(() -> outputPort(null))
                .rebootInputPort(() -> inputPort());
    }

    private static <T> OutputPort<T> outputPort(T value) {
        return () -> value;
    }

    private static <T> InputPort<T> inputPort() {
        return ignored -> { };
    }
}
