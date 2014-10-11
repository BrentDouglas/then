package io.machinecode.then.core;

/**
 * A promise that is set to the {@link #RESOLVED} terminal state when constructed.
 *
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 * @since 1.0
 */
public class ResolvedPromise<T,F extends Throwable> extends PromiseImpl<T,F> {

    public ResolvedPromise(final T value) {
        resolve(value);
    }
}