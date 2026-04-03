package pl.vtt.wpi.core.domain.model.device;

import pl.vtt.wpi.core.domain.model.Color;

import java.util.List;

public record PixelProgram(int index, List<Color> pixels) {
}
