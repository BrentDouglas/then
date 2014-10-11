package io.machinecode.then.core;

/**
 * <p>A promise that is set to the {@link #CANCELLED} terminal state when constructed.</p>
 *
 * @author Brent Douglas (brent.n.douglas@gmail.com)
 * @since 1.0
 */
public class CancelledDeferred<T,F extends Throwable,P> extends DeferredImpl<T,F,P> {

    protected CancelledDeferred(final boolean interrupt) {
        cancel(interrupt);
    }
}