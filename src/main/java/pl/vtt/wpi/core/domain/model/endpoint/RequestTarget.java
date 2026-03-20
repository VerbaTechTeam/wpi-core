package pl.vtt.wpi.core.domain.model.endpoint;

import java.util.EnumSet;
import java.util.Set;

public enum RequestTarget {

    INFO(Resource.INFO, EnumSet.of(Method.GET),
            EnumSet.noneOf(UserGroup.class), false),
    LOGS_READ(Resource.LOGS, EnumSet.of(Method.GET),
            EnumSet.noneOf(UserGroup.class), false),
    LOGS_DELETE(Resource.LOGS, EnumSet.of(Method.DELETE),
            EnumSet.of(UserGroup.ADMIN), true),
    RESTART(Resource.RESTART, EnumSet.of(Method.POST),
            EnumSet.of(UserGroup.ADMIN, UserGroup.EDITOR), true),
    AUTH(Resource.AUTH, EnumSet.of(Method.POST),
            EnumSet.noneOf(UserGroup.class), true),
    USER_PASS(Resource.USER_PASS, EnumSet.of(Method.PATCH),
            EnumSet.noneOf(UserGroup.class), true),
    USERS_READ(Resource.USERS, EnumSet.of(Method.GET),
            EnumSet.of(UserGroup.ADMIN), false),
    USERS_CREATE(Resource.USERS, EnumSet.of(Method.POST),
            EnumSet.of(UserGroup.ADMIN), true),
    USER_DELETE(Resource.USER_BY_NAME, EnumSet.of(Method.DELETE),
            EnumSet.of(UserGroup.ADMIN), true),
    ADMIN_RESET(Resource.ADMIN_RESET, EnumSet.of(Method.POST),
            EnumSet.noneOf(UserGroup.class), true),
    DATA_READ(Resource.DATA, EnumSet.of(Method.GET),
            EnumSet.noneOf(UserGroup.class), false),
    DATA_UPDATE(Resource.DATA, EnumSet.of(Method.PUT, Method.PATCH),
            EnumSet.of(UserGroup.ADMIN, UserGroup.EDITOR), true),
    CURRENT_STATE(Resource.CURRENT_STATE, EnumSet.of(Method.GET),
            EnumSet.noneOf(UserGroup.class), false),
    PIXEL_PROGRAMS_READ(Resource.PIXEL_PROGRAMS, EnumSet.of(Method.GET),
            EnumSet.noneOf(UserGroup.class), false),
    PIXEL_PROGRAMS_UPDATE(Resource.PIXEL_PROGRAMS, EnumSet.of(Method.PUT, Method.PATCH),
            EnumSet.of(UserGroup.ADMIN, UserGroup.DESIGNER), true),
    WIFI_UPDATE(Resource.WIFI_CONFIG, EnumSet.of(Method.PATCH),
            EnumSet.of(UserGroup.ADMIN), true);

    private final EndpointDescriptor descriptor;
    
    RequestTarget(Resource resource, EnumSet<Method> allowedMethods,
                  EnumSet<UserGroup> requiredAnyGroups, boolean mutating) {
        this.descriptor = new EndpointDescriptor(
                resource, allowedMethods, requiredAnyGroups, mutating
        );
    }
    
    public EndpointDescriptor descriptor() {
        return descriptor;
    }

    public boolean mutating() {
        return descriptor().mutating();
    }

    public boolean secured() {
        return !descriptor().isPublic();
    }

    private boolean rejected(Set<UserGroup> groups) {
        return groups.stream().noneMatch(descriptor().requiredAnyGroups()::contains);
    }

    public boolean allow(Method method, Set<UserGroup> userGroups) {
        if (secured() && rejected(userGroups)) {
            return false;
        }
        return descriptor().allowedMethods().contains(method);
    }
}
