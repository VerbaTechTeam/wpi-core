package pl.vtt.wpi.core.domain.port;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestHandler<Void, List<User>> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return List.of(new User("admin", Set.of(UserGroup.ADMIN)));
        };
        UsersOutputPort port = new UsersOutputPort(requestFactory, requestHandler);

        assertEquals("admin", port.load().getFirst().username());
    }
}
