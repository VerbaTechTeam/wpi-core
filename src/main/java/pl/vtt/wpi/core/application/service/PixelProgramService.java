package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.application.exception.PixelProgramOperationException;
import pl.vtt.wpi.core.domain.model.color.RgbColor;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;

import java.util.List;

public interface PixelProgramService {
    List<PixelProgram> getAll() throws PixelProgramOperationException;
    void insert(List<PixelProgram> pixelPrograms) throws PixelProgramOperationException;
    void set(List<PixelProgram> pixelPrograms)
            throws DataInconsistencyException;

    PixelProgram get(int index)
            throws PixelProgramNotFoundException, PixelProgramOperationException;
    PixelProgram singleton(List<RgbColor> pixelProgram);
    PixelProgram save(List<RgbColor> pixelProgram) throws PixelProgramOperationException;
    PixelProgram update(int index, List<RgbColor> pixelProgram)
            throws PixelProgramNotFoundException, PixelProgramOperationException;
    PixelProgram remove(int index)
            throws PixelProgramNotFoundException, DataInconsistencyException, PixelProgramOperationException;
}
