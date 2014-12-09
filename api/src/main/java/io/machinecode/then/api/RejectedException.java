package io.machinecode.then.api;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class RejectedException extends CompletionException {

    public RejectedException(final String message) {
        super(message);
    }
}
