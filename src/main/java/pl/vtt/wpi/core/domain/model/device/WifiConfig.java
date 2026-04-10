package pl.vtt.wpi.core.domain.model.device;

public record WifiConfig(String ssid, String password) {
    @Override
    public String toString() {
        return "WifiConfig[ssid=%s, password=***]".formatted(ssid);
    }
}
