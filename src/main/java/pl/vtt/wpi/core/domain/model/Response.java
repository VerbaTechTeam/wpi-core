package pl.vtt.wpi.core.domain.model;

public final class Response {
    private final String body;
    private final Exception exception;

    public Response(String body, Exception exception) {
        if ((body == null) == (exception == null)) {
            throw new IllegalArgumentException(
                    "Exactly one of body or exception must be non-null"
            );
        }
        this.body = body;
        this.exception = exception;
    }

    public String body() throws Exception {
        if (exception != null) {
            throw exception;
        }
        return body;
    }
}
