package pl.vtt.wpi.core.domain.model.color;

public record HsbColor(int hue, int saturation, int brightness) {
    public HsbColor {
        if (hue < 0 || hue > 360) {
            throw new IllegalArgumentException("hue must be between 0 and 360");
        }
        if (saturation < 0 || saturation > 255) {
            throw new IllegalArgumentException("saturation must be between 0 and 255");
        }
        if (brightness < 0 || brightness > 255) {
            throw new IllegalArgumentException("brightness must be between 0 and 255");
        }
    }

    public RgbColor toRgb() {
        double s = saturation / 255.0;
        double v = brightness / 255.0;
        double c = v * s;
        double x = c * (1 - Math.abs((hue / 60.0) % 2 - 1));
        double m = v - c;

        double r, g, b;
        if (hue < 60) {
            r = c; g = x; b = 0;
        } else if (hue < 120) {
            r = x; g = c; b = 0;
        } else if (hue < 180) {
            r = 0; g = c; b = x;
        } else if (hue < 240) {
            r = 0; g = x; b = c;
        } else if (hue < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        int red = (int) Math.round((r + m) * 255);
        int green = (int) Math.round((g + m) * 255);
        int blue = (int) Math.round((b + m) * 255);

        return new RgbColor(red, green, blue);
    }
}
