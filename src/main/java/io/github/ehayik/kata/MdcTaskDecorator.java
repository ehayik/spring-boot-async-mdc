package io.github.ehayik.kata;

import jakarta.annotation.Nonnull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

/**
 * A decorator for {@link Runnable} tasks that preserves the MDC (Mapped Diagnostic Context) across different threads.
 * This is useful for ensuring that context information, such as logging data, is maintained correctly in asynchronous tasks.
 */
@Component
class MdcTaskDecorator implements TaskDecorator {

    /**
     * Decorates the given {@link Runnable} to ensure that the MDC context is copied and restored correctly.
     *
     * @param runnable the original {@link Runnable} task to be decorated
     * @return a new {@link Runnable} that sets up the MDC context before running the original task and clears it afterward
     */
    @Nonnull
    @Override
    public Runnable decorate(@Nonnull Runnable runnable) {
        // Copy the current MDC context
        var contextMapCopy = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // Set the MDC context if it's not null
                if (contextMapCopy != null) {
                    MDC.setContextMap(contextMapCopy);
                }

                // Execute the original runnable
                runnable.run();
            } finally {
                // Clear the MDC context to avoid context leakage
                MDC.clear();
            }
        };
    }
}