package pl.vtt.wpi.core.domain.model.device;

public record AccessPointConfig(String ssid, String password) {
    @Override
    public String toString() {
        return "AccessPointConfig[ssid=%s, password=***]".formatted(ssid);
    }
}
