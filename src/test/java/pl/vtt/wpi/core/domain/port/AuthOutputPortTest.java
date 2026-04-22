package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.output.AuthOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthOutputPortTest {
    @Test
    void load_usesPostMethod() throws Exception {
        RequestFactory<Void> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestHandler<Void, Credentials> requestHandler = request -> {
            assertEquals(Method.POST, request.method());
            return new Credentials("admin", "token");
        };
        AuthOutputPort port = new AuthOutputPort(requestFactory, requestHandler);

        Credentials credentials = port.load();

        assertEquals("admin", credentials.username());
    }

    @Test
    void load_wrapsException() {
        IllegalStateException originalCause = new IllegalStateException("boom");
        RequestFactory<Void> requestFactory = (_, _, _, _) -> null;
        RequestHandler<Void, Credentials> requestHandler = _ -> { throw originalCause; };
        AuthOutputPort port = new AuthOutputPort(requestFactory, requestHandler);

        OutputPortException exception = assertThrows(OutputPortException.class, port::load);
        assertEquals("Cannot authorize user", exception.getMessage());
        assertSame(originalCause, exception.getCause());
    }

    @Test
    void load_wrapsFactoryExceptionWithCause() {
        RuntimeException originalCause = new RuntimeException("factory-failure");
        RequestFactory<Void> requestFactory = (_, _, _, _) -> {
            throw originalCause;
        };
        RequestHandler<Void, Credentials> requestHandler = _ -> new Credentials("admin", "token");
        AuthOutputPort port = new AuthOutputPort(requestFactory, requestHandler);

        OutputPortException exception = assertThrows(OutputPortException.class, port::load);
        assertEquals("Cannot authorize user", exception.getMessage());
        assertSame(originalCause, exception.getCause());
    }
}
