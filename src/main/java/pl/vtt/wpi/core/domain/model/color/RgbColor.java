package pl.vtt.wpi.core.domain.model.color;

public record RgbColor(int red, int green, int blue) {
    public RgbColor {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException("Red value must be between 0 and 255");
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException("Green value must be between 0 and 255");
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Blue value must be between 0 and 255");
        }
    }

    public int[] toArray() {
        return new int[]{red, green, blue};
    }
}
