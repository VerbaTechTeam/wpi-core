package pl.vtt.wpi.core.domain.model.endpoint;

public enum Resource {
    // Publiczne
    INFO("/api/info", Group.CORE),
    LOGS("/api/logs", Group.CORE),
    DATA("/api/data", Group.CORE),
    CURRENT_STATE("/api/current-state", Group.CORE),
    PIXEL_PROGRAMS("/api/pixelprograms", Group.CORE),

    // Bezpieczeństwo / Auth
    AUTH("/api/secure/auth", Group.AUTH),
    USER_PASS("/api/secure/pass", Group.SECURE),
    ADMIN_CONFIG("/api/secure/admin", Group.SECURE),
    USERS("/api/secure/users", Group.SECURE),
    USER_BY_NAME("/api/secure/users/%s", Group.SECURE),
    ADMIN_RESET("/api/secure/admin/reset", Group.SECURE),

    // Zarządzanie systemem
    RESTART("/api/restart", Group.SYSTEM),
    WIFI_CONFIG("/api/wifi", Group.SYSTEM);

    private final String path;
    private final Group group;

    Resource(String path, Group group) {
        this.path = path;
        this.group = group;
    }

    public Group group() {
        return group;
    }

    public String url(String baseUrl, Object... args) {
        if (baseUrl == null) {
            return path.formatted(args);
        }
        return (baseUrl + path).formatted(args);
    }

    public enum Group {
        AUTH,
        CORE,
        SECURE,
        SYSTEM
    }
}