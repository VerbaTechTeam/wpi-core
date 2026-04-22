package pl.vtt.wpi.core.domain.model.endpoint;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

    private static final String SUPPORTED_CONVERSIONS = "bhscdoxXfEeGgaAtT%";
    private final String path;
    private final Group group;

    Resource(String path, Group group) {
        this.path = path;
        this.group = group;
    }

    public Group group() {
        return group;
    }

    public int pathVariableCount() {
        int count = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '%' && i + 1 < path.length()) {
                char nextChar = path.charAt(i + 1);
                if (SUPPORTED_CONVERSIONS.indexOf(nextChar) >= 0) {
                    count++;
                } else if (!isHexDigit(nextChar)) {
                    for (int j = i + 1; j < path.length(); j++) {
                        char c = path.charAt(j);
                        if (SUPPORTED_CONVERSIONS.indexOf(c) >= 0) {
                            count++;
                            break;
                        } else if (!isFormatModifier(c)) {
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean isFormatModifier(char c) {
        return c == '-' || c == '+' || c == '#' || c == '(' || c == ',' || c == '.' ||
               (c >= '0' && c <= '9');
    }

    String url(String baseUrl, Object... args) {
        args = Arrays.stream(args)
                .map(arg -> URLEncoder
                        .encode(String.valueOf(arg), StandardCharsets.UTF_8)
                        .replace("+", "%20"))
                .toArray();
        int requiredArgs = pathVariableCount();
        if (args.length != requiredArgs) {
            throw new IllegalArgumentException("Expected " + requiredArgs
                    + " arguments but got " + args.length);
        }
        String formatted = requiredArgs == 0 ? path : path.formatted(args);
        return baseUrl == null ? formatted : baseUrl + formatted;
    }

    public enum Group {
        AUTH,
        CORE,
        SECURE,
        SYSTEM
    }
}