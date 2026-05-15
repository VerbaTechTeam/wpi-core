package pl.vtt.wpi.core.domain.port;

import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;
import pl.vtt.wpi.core.domain.port.output.UsersOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestHandler<Void, List<User>> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return List.of(new User("admin", EnumSet.of(UserGroup.ADMIN)));
        };
        UsersOutputPort port = new UsersOutputPort(requestFactory, requestHandler);

        assertEquals("admin", port.load().getFirst().username());
    }
}
