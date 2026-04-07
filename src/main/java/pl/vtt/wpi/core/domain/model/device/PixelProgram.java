package pl.vtt.wpi.core.domain.model.device;

import pl.vtt.wpi.core.domain.model.color.RgbColor;

import java.util.List;

public record PixelProgram(int index, List<RgbColor> pixels) {
}
