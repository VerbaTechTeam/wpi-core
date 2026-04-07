package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.domain.model.Color;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;

import java.util.List;

public interface PixelProgramService {
    List<PixelProgram> getAll();
    void insert(List<PixelProgram> pixelPrograms);
    void set(List<PixelProgram> pixelPrograms)
            throws DataInconsistencyException;

    PixelProgram get(int index)
            throws PixelProgramNotFoundException;
    PixelProgram singleton(List<Color> pixelProgram);
    PixelProgram save(List<Color> pixelProgram);
    PixelProgram update(int index, List<Color> pixelProgram)
            throws PixelProgramNotFoundException;
    PixelProgram remove(int index)
            throws PixelProgramNotFoundException, DataInconsistencyException;
}
