package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.exception.DataInconsistencyException;
import pl.vtt.wpi.core.application.exception.PixelProgramNotFoundException;
import pl.vtt.wpi.core.application.exception.PixelProgramServiceException;
import pl.vtt.wpi.core.domain.model.color.RgbColor;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;

import java.util.List;

public interface PixelProgramService {
    List<PixelProgram> getAll() throws PixelProgramServiceException;
    void insert(List<PixelProgram> pixelPrograms) throws PixelProgramServiceException;
    void set(List<PixelProgram> pixelPrograms)
            throws DataInconsistencyException;

    PixelProgram get(int index)
            throws PixelProgramNotFoundException, PixelProgramServiceException;
    PixelProgram singleton(List<RgbColor> pixelProgram);
    PixelProgram save(List<RgbColor> pixelProgram) throws PixelProgramServiceException;
    PixelProgram update(int index, List<RgbColor> pixelProgram)
            throws PixelProgramNotFoundException, PixelProgramServiceException;
    PixelProgram remove(int index)
            throws PixelProgramNotFoundException, DataInconsistencyException, PixelProgramServiceException;
}
