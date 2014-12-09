package io.machinecode.then.api;

/**
 * <p>Listener for a {@link Promise} entering a {@link Deferred#CANCELLED} terminal state.</p>
 *
 * @see Promise
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface OnCancel {

    /**
     * @param mayInterrupt If a running computation may be interrupted.
     * @return {@code true} If the promise was cancelled, {@code false} if it had already reached another terminal state.
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    boolean cancel(final boolean mayInterrupt);
}
