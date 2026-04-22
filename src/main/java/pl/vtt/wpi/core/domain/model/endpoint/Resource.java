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
            if (path.charAt(i) != '%' || i + 1 >= path.length()) {
                continue;
            }
            int j = i + 1;
            // skip flags / width / precision / argument-index prefix
            while (j < path.length() && isFormatModifier(path.charAt(j))) {
                j++;
            }
            if (j >= path.length()) {
                break;
            }
            char conv = path.charAt(j);
            if (conv == '%' || conv == 'n') {
                // literal % or newline — no argument consumed
                i = j;
            } else if (SUPPORTED_CONVERSIONS.indexOf(conv) >= 0) {
                count++;
                i = j;
            }
        }
        return count;
    }

    private boolean isFormatModifier(char c) {
        return c == '-' || c == '+' || c == '#' || c == '(' || c == ',' || c == '.' ||
               (c >= '0' && c <= '9');
    }

    String url(String baseUrl, Object... args) {
        int requiredArgs = pathVariableCount();
        if (args.length != requiredArgs) {
            throw new IllegalArgumentException("Expected " + requiredArgs
                    + " arguments but got " + args.length);
        }
        args = Arrays.stream(args)
                .map(arg -> URLEncoder
                        .encode(String.valueOf(arg), StandardCharsets.UTF_8)
                        .replace("+", "%20"))
                .toArray();
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