package pl.vtt.wpi.core.domain.model.endpoint;

public enum UserGroup {
    ADMIN("admin"),
    DESIGNER("designer"),
    EDITOR("editor");

    private final String id;

    UserGroup(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
