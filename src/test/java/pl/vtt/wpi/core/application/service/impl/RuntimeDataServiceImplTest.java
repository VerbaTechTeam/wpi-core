package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuntimeDataServiceImplTest {

    @Test
    void set_nullRuntimeData_throwsNullPointerException() {
        RuntimeDataServiceImpl service = new RuntimeDataServiceImpl(() -> null, () -> List.of(), _ -> {});

        NullPointerException exception = assertThrows(NullPointerException.class, () -> service.set(null));

        assertEquals("Runtime data cannot be null", exception.getMessage());
    }

    @Test
    void set_missingPixelProgram_throwsDataInconsistencyExceptionWithNoSuchElementCause() {
        RuntimeData runtimeData = new RuntimeData.Builder().pixelProgram(10).build();
        RuntimeDataServiceImpl service = new RuntimeDataServiceImpl(
                () -> runtimeData,
                () -> List.of(new PixelProgram(0, List.of())),
                _ -> {}
        );

        DataInconsistencyException exception = assertThrows(DataInconsistencyException.class,
                () -> service.set(runtimeData));

        assertEquals("Pixel program 10 does not exist", exception.getMessage());
        assertInstanceOf(NoSuchElementException.class, exception.getCause());
    }

    @Test
    void set_existingPixelProgram_sendsRuntimeData() throws Exception {
        RuntimeData runtimeData = new RuntimeData.Builder().pixelProgram(1).build();
        AtomicReference<RuntimeData> sent = new AtomicReference<>();
        RuntimeDataServiceImpl service = new RuntimeDataServiceImpl(
                () -> runtimeData,
                () -> List.of(new PixelProgram(1, List.of())),
                sent::set
        );

        service.set(runtimeData);

        assertEquals(runtimeData, sent.get());
    }
}
