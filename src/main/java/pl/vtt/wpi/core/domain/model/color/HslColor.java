package pl.vtt.wpi.core.domain.model.color;

public record HslColor(int hue, int saturation, int lightness) {
    public HslColor {
        if (hue < 0 || hue > 360) {
            throw new IllegalArgumentException("Hue value must be between 0 and 360");
        }
        if (saturation < 0 || saturation > 100) {
            throw new IllegalArgumentException("Saturation value must be between 0 and 100");
        }
        if (lightness < 0 || lightness > 100) {
            throw new IllegalArgumentException("Lightness value must be between 0 and 100");
        }
    }

    public RgbColor toRgb() {
        double s = saturation / 100.0;
        double l = lightness / 100.0;
        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((hue / 60.0) % 2 - 1));
        double m = l - c / 2.0;

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
