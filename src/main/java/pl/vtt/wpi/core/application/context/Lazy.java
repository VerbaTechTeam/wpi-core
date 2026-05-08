package pl.vtt.wpi.core.application.context;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private volatile Supplier<? extends T> initializer;
    private volatile T value;

    private Lazy(Supplier<? extends T> initializer) {
        this.initializer = Objects.requireNonNull(initializer, "initializer cannot be null");
    }

    public static <T> Lazy<T> of(Supplier<? extends T> initializer) {
        return new Lazy<>(initializer);
    }

    @Override
    public T get() {
        Supplier<? extends T> currentInitializer = initializer;
        if (currentInitializer == null) {
            return value;
        }
        synchronized (this) {
            currentInitializer = initializer;
            if (currentInitializer == null) {
                return value;
            }
            T current = Objects.requireNonNull(currentInitializer.get(),
                    "initializer cannot return null");
            value = current;
            initializer = null;
            return current;
        }
    }
}
