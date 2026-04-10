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
        int c = Math.toIntExact(Math.round((1 - (color.red() / 255.0)) * 100));
        int m = Math.toIntExact(Math.round((1 - (color.green() / 255.0)) * 100));
        int y = Math.toIntExact(Math.round((1 - (color.blue() / 255.0)) * 100));
        float brightness = Math.max(color.red(), Math.max(color.green(), color.blue())) / 255.0f;
        int k = Math.toIntExact(Math.round((1 - brightness) * 100));
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
