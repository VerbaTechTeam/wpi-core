package pl.vtt.wpi.core.domain.model.color;

public record CmykColor(int cyan, int magenta, int yellow, int key) {
    public CmykColor {
        if (cyan < 0 || cyan > 100) {
            throw new IllegalArgumentException("Cyan value must be between 0 and 100");
        }
        if (magenta < 0 || magenta > 100) {
            throw new IllegalArgumentException("Magenta value must be between 0 and 100");
        }
        if (yellow < 0 || yellow > 100) {
            throw new IllegalArgumentException("Yellow value must be between 0 and 100");
        }
        if (key < 0 || key > 100) {
            throw new IllegalArgumentException("Key value must be between 0 and 100");
        }
    }

    public RgbColor toRgb() {
        int r = Math.toIntExact(Math.round((1 - (cyan / 100.0)) * (1 - (key / 100.0)) * 255));
        int g = Math.toIntExact(Math.round((1 - (magenta / 100.0)) * (1 - (key / 100.0)) * 255));
        int b = Math.toIntExact(Math.round((1 - (yellow / 100.0)) * (1 - (key / 100.0)) * 255));
        return new RgbColor(r, g, b);
    }
}
