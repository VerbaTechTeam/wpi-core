package pl.vtt.wpi.core.application.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.application.service.PixelProgramService;
import pl.vtt.wpi.core.domain.model.color.RgbColor;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

public class PixelProgramServiceImpl implements PixelProgramService {
    private final OutputPort<List<PixelProgram>> pixelProgramsOutputPort;
    private final InputPort<List<PixelProgram>> pixelProgramsInputPort;

    public PixelProgramServiceImpl(OutputPort<List<PixelProgram>> pixelProgramsOutputPort,
                                   InputPort<List<PixelProgram>> pixelProgramsInputPort) {
        this.pixelProgramsOutputPort = Objects.requireNonNull(pixelProgramsOutputPort,
                "pixelProgramsOutputPort cannot be null");
        this.pixelProgramsInputPort = Objects.requireNonNull(pixelProgramsInputPort,
                "pixelProgramsInputPort cannot be null");
    }

    @Override
    public List<PixelProgram> getAll() {
        return new ArrayList<>(loadPrograms());
    }

    @Override
    public void insert(List<PixelProgram> pixelPrograms) {
        List<PixelProgram> current = getAll();
        if (pixelPrograms == null || pixelPrograms.isEmpty()) {
            return;
        }
        current.addAll(pixelPrograms);
        try {
            set(current);
        } catch (DataInconsistencyException e) {
            throw new RuntimeException("Cannot insert pixel programs", e);
        }
    }

    @Override
    public void set(List<PixelProgram> pixelPrograms) throws DataInconsistencyException {
        if (pixelPrograms == null) {
            throw new DataInconsistencyException("Pixel programs cannot be null");
        }
        try {
            pixelProgramsInputPort.send(List.copyOf(pixelPrograms));
        } catch (InputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new DataInconsistencyException("Cannot update pixel programs", cause);
        }
    }

    @Override
    public PixelProgram get(int index) throws PixelProgramNotFoundException {
        return loadPrograms().stream()
                .filter(pixelProgram -> pixelProgram.index() == index)
                .findFirst()
                .orElseThrow(() -> new PixelProgramNotFoundException("Pixel program %d not found".formatted(index)));
    }

    @Override
    public PixelProgram singleton(List<RgbColor> pixelProgram) {
        return new PixelProgram(0, pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
    }

    @Override
    public PixelProgram save(List<RgbColor> pixelProgram) {
        List<PixelProgram> programs = getAll();
        int nextIndex = programs.stream()
                .map(PixelProgram::index)
                .max(Comparator.naturalOrder())
                .map(i -> i + 1)
                .orElse(0);
        PixelProgram saved = new PixelProgram(nextIndex,
                pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
        programs.add(saved);
        try {
            set(programs);
        } catch (DataInconsistencyException e) {
            throw new RuntimeException("Cannot save pixel program", e);
        }
        return saved;
    }

    @Override
    public PixelProgram update(int index, List<RgbColor> pixelProgram) throws PixelProgramNotFoundException {
        List<PixelProgram> programs = getAll();
        int replaceIndex = findListIndex(programs, index);
        PixelProgram updated = new PixelProgram(index,
                pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
        programs.set(replaceIndex, updated);
        try {
            set(programs);
        } catch (DataInconsistencyException e) {
            throw new RuntimeException("Cannot update pixel program", e);
        }
        return updated;
    }

    @Override
    public PixelProgram remove(int index) throws PixelProgramNotFoundException, DataInconsistencyException {
        List<PixelProgram> programs = getAll();
        int removeIndex = findListIndex(programs, index);
        PixelProgram removed = programs.remove(removeIndex);
        set(programs);
        return removed;
    }

    private List<PixelProgram> loadPrograms() {
        try {
            List<PixelProgram> pixelPrograms = pixelProgramsOutputPort.load();
            return pixelPrograms == null ? List.of() : pixelPrograms;
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Cannot read pixel programs", cause);
        }
    }

    private int findListIndex(List<PixelProgram> programs, int index) throws PixelProgramNotFoundException {
        for (int i = 0; i < programs.size(); i++) {
            PixelProgram program = programs.get(i);
            if (program != null && program.index() == index) {
                return i;
            }
        }
        throw new PixelProgramNotFoundException("Pixel program %d not found".formatted(index));
    }
}
