package pl.vtt.wpi.core.application.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PixelProgramServiceImplTest {

    @Test
    void set_nullElement_throwsDataInconsistencyException() {
        PixelProgramServiceImpl service = new PixelProgramServiceImpl(() -> List.of(), _ -> {});

        DataInconsistencyException exception = assertThrows(DataInconsistencyException.class,
                () -> service.set(new java.util.ArrayList<>(java.util.Collections.singletonList(null))));

        assertEquals("Pixel programs cannot contain null elements", exception.getMessage());
    }

    @Test
    void save_addsProgramWithNextIndexAndSendsAll() {
        AtomicReference<List<PixelProgram>> sent = new AtomicReference<>();
        PixelProgramServiceImpl service = new PixelProgramServiceImpl(
                () -> List.of(new PixelProgram(0, List.of())),
                sent::set
        );

        PixelProgram saved = service.save(List.of());

        assertEquals(1, saved.index());
        assertEquals(2, sent.get().size());
    }

    @Test
    void get_missingProgram_throwsPixelProgramNotFoundException() {
        PixelProgramServiceImpl service = new PixelProgramServiceImpl(() -> List.of(), _ -> {});

        assertThrows(PixelProgramNotFoundException.class, () -> service.get(7));
    }
}
