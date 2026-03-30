package pl.vtt.wpi.core.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeserializationExceptionTest {

    @Test
    @DisplayName("Should store message and cause")
    void shouldStoreMessageAndCause() {
        Throwable cause = new RuntimeException("root cause");
        DeserializationException ex = new DeserializationException("deserialization failed", cause);

        assertAll(
                () -> assertEquals("deserialization failed", ex.getMessage()),
                () -> assertSame(cause, ex.getCause())
        );
    }

    @Test
    @DisplayName("Should be a RuntimeException")
    void shouldBeARuntimeException() {
        DeserializationException ex = new DeserializationException("msg", new Exception());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("Should preserve cause type through the hierarchy")
    void shouldPreserveCauseType() {
        IllegalArgumentException cause = new IllegalArgumentException("bad arg");
        DeserializationException ex = new DeserializationException("wrap", cause);

        assertSame(cause, ex.getCause());
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }

    @Test
    @DisplayName("Should allow null message with non-null cause")
    void shouldAllowNullMessage() {
        Throwable cause = new Exception("cause");
        DeserializationException ex = new DeserializationException(null, cause);

        assertAll(
                () -> assertNull(ex.getMessage()),
                () -> assertSame(cause, ex.getCause())
        );
    }
}