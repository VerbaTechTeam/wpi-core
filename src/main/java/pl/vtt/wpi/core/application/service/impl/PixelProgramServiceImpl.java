package pl.vtt.wpi.core.application.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.application.exception.PixelProgramServiceException;
import pl.vtt.wpi.core.application.service.PixelProgramService;
import pl.vtt.wpi.core.domain.model.color.RgbColor;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.port.InputPort;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;

/**
 * Default {@link PixelProgramService} implementation.
 * <p>
 * Mutating operations are synchronized per service instance with a lock to avoid
 * read-modify-write races between {@code load()} and {@code send()} calls.
 * </p>
 */
public class PixelProgramServiceImpl implements PixelProgramService {
    private final OutputPort<List<PixelProgram>> pixelProgramsOutputPort;
    private final InputPort<List<PixelProgram>> pixelProgramsInputPort;
    private final Lock mutationLock = new ReentrantLock();

    public PixelProgramServiceImpl(OutputPort<List<PixelProgram>> pixelProgramsOutputPort,
                                   InputPort<List<PixelProgram>> pixelProgramsInputPort) {
        this.pixelProgramsOutputPort = Objects.requireNonNull(pixelProgramsOutputPort,
                "pixelProgramsOutputPort cannot be null");
        this.pixelProgramsInputPort = Objects.requireNonNull(pixelProgramsInputPort,
                "pixelProgramsInputPort cannot be null");
    }

    @Override
    public List<PixelProgram> getAll() throws PixelProgramServiceException {
        return new ArrayList<>(loadPrograms());
    }

    @Override
    public void insert(List<PixelProgram> pixelPrograms) throws PixelProgramServiceException {
        if (pixelPrograms == null || pixelPrograms.isEmpty()) {
            return;
        }
        mutationLock.lock();
        try {
            List<PixelProgram> current = getAll();
            current.addAll(pixelPrograms);
            set(current);
        } catch (DataInconsistencyException e) {
            throw new PixelProgramServiceException("Cannot insert pixel programs", e);
        } finally {
            mutationLock.unlock();
        }
    }

    @Override
    public void set(List<PixelProgram> pixelPrograms) throws DataInconsistencyException {
        mutationLock.lock();
        try {
            if (pixelPrograms == null) {
                throw new DataInconsistencyException("Pixel programs cannot be null");
            }
            if (pixelPrograms.stream().anyMatch(Objects::isNull)) {
                throw new DataInconsistencyException("Pixel programs cannot contain null elements");
            }
            try {
                pixelProgramsInputPort.send(List.copyOf(pixelPrograms));
            } catch (InputPortException e) {
                Throwable cause = e.getCause() == null ? e : e.getCause();
                throw new DataInconsistencyException("Cannot update pixel programs", cause);
            }
        } finally {
            mutationLock.unlock();
        }
    }

    @Override
    public PixelProgram get(int index) throws PixelProgramNotFoundException, PixelProgramServiceException {
        List<PixelProgram> programs = loadPrograms();
        return programs.get(findListIndex(programs, index));
    }

    @Override
    public PixelProgram singleton(List<RgbColor> pixelProgram) {
        return new PixelProgram(0, pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
    }

    @Override
    public PixelProgram save(List<RgbColor> pixelProgram) throws PixelProgramServiceException {
        mutationLock.lock();
        try {
            List<PixelProgram> programs = new ArrayList<>(getAll());
            int nextIndex = programs.stream()
                    .map(PixelProgram::index)
                    .max(Comparator.naturalOrder())
                    .map(i -> i + 1)
                    .orElse(0);
            PixelProgram saved = new PixelProgram(nextIndex,
                    pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
            programs.add(saved);
            set(programs);
            return saved;
        } catch (DataInconsistencyException e) {
            throw new PixelProgramServiceException("Cannot save pixel program", e);
        } finally {
            mutationLock.unlock();
        }
    }

    @Override
    public PixelProgram update(int index, List<RgbColor> pixelProgram)
            throws PixelProgramNotFoundException, PixelProgramServiceException {
        mutationLock.lock();
        try {
            List<PixelProgram> programs = getAll();
            int replaceIndex = findListIndex(programs, index);
            PixelProgram updated = new PixelProgram(index,
                    pixelProgram == null ? List.of() : List.copyOf(pixelProgram));
            programs.set(replaceIndex, updated);
            set(programs);
            return updated;
        } catch (DataInconsistencyException e) {
            throw new PixelProgramServiceException("Cannot update pixel program", e);
        } finally {
            mutationLock.unlock();
        }
    }

    @Override
    public PixelProgram remove(int index)
            throws PixelProgramNotFoundException, DataInconsistencyException, PixelProgramServiceException {
        mutationLock.lock();
        try {
            List<PixelProgram> programs = getAll();
            int removeIndex = findListIndex(programs, index);
            PixelProgram removed = programs.remove(removeIndex);
            set(programs);
            return removed;
        } catch (PixelProgramServiceException e) {
            throw e;
        } finally {
            mutationLock.unlock();
        }
    }

    private List<PixelProgram> loadPrograms() throws PixelProgramServiceException {
        try {
            List<PixelProgram> pixelPrograms = pixelProgramsOutputPort.load();
            return pixelPrograms == null ? List.of() : pixelPrograms;
        } catch (OutputPortException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new PixelProgramServiceException("Cannot read pixel programs", cause);
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
