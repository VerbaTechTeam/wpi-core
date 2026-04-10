package pl.vtt.wpi.core.domain.model.color;

public final class Color {
    private Color() {
        throw new AssertionError("No Color instances for you!");
    }

    public static RgbColor rgb(int r, int g, int b) {
        return new RgbColor(r, g, b);
    }

    public static RgbColor rgb(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new RgbColor(r, g, b);
    }

    public static CmykColor cmyk(int cyan, int magenta, int yellow, int key) {
        return new CmykColor(cyan, magenta, yellow, key);
    }

    public static CmykColor cmyk(int rgb) {
        RgbColor color = rgb(rgb);
        double r = color.red() / 255.0;
        double g = color.green() / 255.0;
        double b = color.blue() / 255.0;
        
        double kNorm = 1.0 - Math.max(r, Math.max(g, b));
        int k = (int) Math.round(kNorm * 100);
        int c, m, y;
        if (kNorm >= 1.0) {
            c = 0; m = 0; y = 0;
        } else {
            c = (int) Math.round(((1.0 - r - kNorm) / (1.0 - kNorm)) * 100);
            m = (int) Math.round(((1.0 - g - kNorm) / (1.0 - kNorm)) * 100);
            y = (int) Math.round(((1.0 - b - kNorm) / (1.0 - kNorm)) * 100);
        }
        return new CmykColor(c, m, y, k);
    }

    public static HslColor hsl(int hue, int saturation, int lightness) {
        return new HslColor(hue, saturation, lightness);
    }

    public static HsbColor hsb(int hue, int saturation, int brightness) {
        return new HsbColor(hue, saturation, brightness);
    }

    public static LabColor lab(float l, float a, float b) {
        return new LabColor(l, a, b);
    }
}
