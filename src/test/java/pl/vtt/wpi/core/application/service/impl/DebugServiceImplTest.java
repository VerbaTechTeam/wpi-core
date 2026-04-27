package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.DebugServiceException;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DebugServiceImplTest {

    @Test
    void pollLogs_readsAndClearsLogs() throws Exception {
        OutputPort<List<String>> logsOutputPort = () -> List.of("a", "b");
        AtomicBoolean cleared = new AtomicBoolean(false);
        InputPort<Void> deleteInputPort = _ -> cleared.set(true);

        DebugServiceImpl service = new DebugServiceImpl(logsOutputPort, deleteInputPort,
                () -> new CurrentState.Builder().build());

        List<String> logs = service.pollLogs();

        assertEquals(List.of("a", "b"), logs);
        assertTrue(cleared.get());
    }

    @Test
    void peekLogs_outputFailure_wrapsException() {
        DebugServiceImpl service = new DebugServiceImpl(
                () -> { throw new OutputPortException("x", new IllegalStateException("boom")); },
                _ -> {},
                () -> new CurrentState.Builder().build()
        );

        DebugServiceException exception = assertThrows(DebugServiceException.class, service::peekLogs);
        assertEquals("Cannot read logs", exception.getMessage());
    }
}
