package io.machinecode.then.core;

import io.machinecode.then.api.OnComplete;
import io.machinecode.then.api.Promise;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A promise that will be resolved when any of the promised passed to it are resolved.
 * If none of them are resolved this promise will be rejected with {@code null}.</p>
 *
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class AnyDeferred<T,F,P> extends DeferredImpl<T,F,P> {

    private static final Logger log = Logger.getLogger(AnyDeferred.class);

    public AnyDeferred(final Collection<? extends Promise<T,?,?>> promises) {
        if (promises.isEmpty()) {
            log.tracef(Messages.get("THEN-000500.promise.none.resolved.in.any"));
            reject(null);
            return;
        }
        final AtomicInteger count = new AtomicInteger(0);
        final OnComplete complete = new OnComplete() {
            @Override
            public void complete(final int state) {
                final int n = count.incrementAndGet();
                if (n == promises.size()) {
                    log.tracef(Messages.get("THEN-000500.promise.none.resolved.in.any"));
                    reject(null);
                }
            }
        };
        for (final Promise<T,?,?> promise : promises) {
            promise.onResolve(this)
                    .onComplete(complete);
        }
    }

    public AnyDeferred(final Promise<T,?,?>... promises) {
        this(Arrays.asList(promises));
    }
}
