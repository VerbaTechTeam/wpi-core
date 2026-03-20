package pl.vtt.wpi.core.domain.model.endpoint;

import java.util.Set;

public record EndpointDescriptor(
        EndpointId id,
        Resource resource,
        Set<Method> allowedMethods,
        Set<UserGroup> requiredAnyGroups,
        boolean mutating
) {
    public EndpointDescriptor {
        allowedMethods = Set.copyOf(allowedMethods);
        requiredAnyGroups = Set.copyOf(requiredAnyGroups);
    }

    public boolean isPublic() {
        return requiredAnyGroups.isEmpty();
    }
}
