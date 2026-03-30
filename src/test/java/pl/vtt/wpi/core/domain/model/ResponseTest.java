package pl.vtt.wpi.core.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    // --- Constructor invariant ---

    @Test
    @DisplayName("Should construct with body and no exception")
    void shouldConstructWithBodyAndNoException() {
        assertDoesNotThrow(() -> new Response("response body", null));
    }

    @Test
    @DisplayName("Should construct with exception and no body")
    void shouldConstructWithExceptionAndNoBody() {
        assertDoesNotThrow(() -> new Response(null, new Exception("error")));
    }

    @Test
    @DisplayName("Should reject both body and exception being null")
    void shouldRejectBothNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Response(null, null)
        );
        assertEquals("Exactly one of body or exception must be non-null", ex.getMessage());
    }

    @Test
    @DisplayName("Should reject both body and exception being non-null")
    void shouldRejectBothNonNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Response("body", new Exception("error"))
        );
        assertEquals("Exactly one of body or exception must be non-null", ex.getMessage());
    }

    @Test
    @DisplayName("Should accept empty string body as valid")
    void shouldAcceptEmptyStringBody() {
        assertDoesNotThrow(() -> new Response("", null));
    }

    // --- body() happy path ---

    @Test
    @DisplayName("Should return body string when no exception is set")
    void shouldReturnBodyString() throws Exception {
        Response response = new Response("expected body", null);
        assertEquals("expected body", response.body());
    }

    @Test
    @DisplayName("Should return empty string body")
    void shouldReturnEmptyBody() throws Exception {
        Response response = new Response("", null);
        assertEquals("", response.body());
    }

    // --- body() sad path ---

    @Test
    @DisplayName("Should throw stored exception when exception is set")
    void shouldThrowStoredException() {
        Exception stored = new Exception("stored exception");
        Response response = new Response(null, stored);

        Exception thrown = assertThrows(Exception.class, response::body);
        assertSame(stored, thrown, "body() must throw the exact same exception instance");
    }

    @Test
    @DisplayName("Should throw RuntimeException stored as exception")
    void shouldThrowRuntimeExceptionStored() {
        RuntimeException stored = new RuntimeException("runtime");
        Response response = new Response(null, stored);

        RuntimeException thrown = assertThrows(RuntimeException.class, response::body);
        assertSame(stored, thrown);
    }

    @Test
    @DisplayName("Should propagate checked exception stored in response")
    void shouldPropagateCheckedExceptionFromBody() {
        Exception checked = new Exception("checked");
        Response response = new Response(null, checked);

        Exception thrown = assertThrows(Exception.class, response::body);
        assertEquals("checked", thrown.getMessage());
    }
}