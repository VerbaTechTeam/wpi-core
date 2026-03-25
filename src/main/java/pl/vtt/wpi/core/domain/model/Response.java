package pl.vtt.wpi.core.domain.model;

public final class Response {
    private final String body;
    private final Exception exception;

    public Response(String body, Exception exception) {
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
