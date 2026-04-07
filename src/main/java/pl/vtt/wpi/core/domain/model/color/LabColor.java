package pl.vtt.wpi.core.domain.model.color;

public record LabColor(float l, float a, float b) {
    public LabColor {
        if (l < 0 || l > 100) {
            throw new IllegalArgumentException("L value must be between 0 and 100");
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
        float y = (l + 16) / 116;
        float x = a / 500 + y;
        float z = y - b / 200;

        x = 95.047f * (x > 0.008856f ? (float) Math.pow(x, 3) : (x - (float) 16 / 116) / 7.787f);
        y = 100.000f * (y > 0.008856f ? (float) Math.pow(y, 3) : (y - (float) 16 / 116) / 7.787f);
        z = 108.883f * (z > 0.008856f ? (float) Math.pow(z, 3) : (z - (float) 16 / 116) / 7.787f);

        // Convert XYZ to RGB
        int r = Math.round(x * 0.4124564f + y * 0.2126729f + z * 0.0193339f);
        int g = Math.round(x * 0.2126729f + y * 0.7151522f + z * 0.1191920f);
        int b = Math.round(x * 0.0193339f + y * 0.1191920f + z * 0.9503041f);

        return new RgbColor(clamp(r), clamp(g), clamp(b));
    }

    private int clamp(int value) {
        return Math.clamp(value, 0, 255);
    }
}
