package pl.vtt.wpi.core.domain.model.color;

public record LabColor(float lightness, float a, float b) {
    public LabColor {
        if (!Float.isFinite(lightness) || !Float.isFinite(a) || !Float.isFinite(b)) {
            throw new IllegalArgumentException("Lab components must be finite numbers");
        }
        if (lightness < 0 || lightness > 100) {
            throw new IllegalArgumentException("Lightness value must be between 0 and 100");
        }
        if (a < -128 || a > 127) {
            throw new IllegalArgumentException("A value must be between -128 and 127");
        }
        if (b < -128 || b > 127) {
            throw new IllegalArgumentException("B value must be between -128 and 127");
        }
    }

    public RgbColor toRgb() {
        // Convert Lab to XYZ
        float y = (lightness + 16) / 116;
        float x = a / 500 + y;
        float z = y - b / 200;

        x = 95.047f * labPivot(x);
        y = 100.000f * labPivot(y);
        z = 108.883f * labPivot(z);

        // Convert XYZ to RGB
        // Convert XYZ to linear RGB (D65, sRGB)
        double xn = x / 100.0;
        double yn = y / 100.0;
        double zn = z / 100.0;

        double rLin =  3.2406 * xn - 1.5372 * yn - 0.4986 * zn;
        double gLin = -0.9689 * xn + 1.8758 * yn + 0.0415 * zn;
        double bLin =  0.0557 * xn - 0.2040 * yn + 1.0570 * zn;

        int r = Math.round((float) (gammaCorrect(rLin) * 255.0));
        int g = Math.round((float) (gammaCorrect(gLin) * 255.0));
        int b = Math.round((float) (gammaCorrect(bLin) * 255.0));

        return new RgbColor(clamp(r), clamp(g), clamp(b));
    }

    private static float labPivot(float t) {
        float t3 = t * t * t;
        return t3 > 0.008856f ? t3 : (t - 16f / 116f) / 7.787f;
    }

    private static double gammaCorrect(double c) {
        c = Math.clamp(c, 0.0, 1.0);
        return c <= 0.0031308 ? 12.92 * c : 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
    }

    private int clamp(int value) {
        return Math.clamp(value, 0, 255);
    }
}
