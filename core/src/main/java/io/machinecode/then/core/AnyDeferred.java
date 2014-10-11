package io.machinecode.then.core;

import io.machinecode.then.api.CompletionException;
import io.machinecode.then.api.OnComplete;
import io.machinecode.then.api.Promise;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A promise that will be resolved when any of the promised passes to it are resolved. If none of them are resolved
 * this promise will be rejected with a {@link CompletionException}.</p>
 *
 * @author Brent Douglas (brent.n.douglas@gmail.com)
 * @since 1.0
 */
public class AnyDeferred<T,P> extends DeferredImpl<T,CompletionException,P> {

    protected AnyDeferred(final Collection<? extends Promise<?,?,?>> promises) {
        if (promises.isEmpty()) {
            reject(new CompletionException(Messages.get("THEN-000019.promise.none.resolved.in.any")));
            return;
        }
        final AtomicInteger count = new AtomicInteger(0);
        for (final Promise<?,?,?> promise : promises) {
            promise.onComplete(new OnComplete() {
                @Override
                public void complete(final int state) {
                    final int n = count.incrementAndGet();
                    if (state == RESOLVED) {
                        resolve(null);
                    } else if (n == promises.size()) {
                        reject(new CompletionException(Messages.get("THEN-000019.promise.none.resolved.in.any")));
                    }
                }
            });
        }
    }

    protected AnyDeferred(final Promise<?,?,?>... promises) {
        if (promises.length == 0) {
            reject(new CompletionException(Messages.get("THEN-000019.promise.none.resolved.in.any")));
            return;
        }
        final AtomicInteger count = new AtomicInteger(0);
        for (final Promise<?,?,?> promise : promises) {
            promise.onComplete(new OnComplete() {
                @Override
                public void complete(final int state) {
                    final int n = count.incrementAndGet();
                    if (state == RESOLVED) {
                        resolve(null);
                    } else if (n == promises.length) {
                        reject(new CompletionException(Messages.get("THEN-000019.promise.none.resolved.in.any")));
                    }
                }
            });
        }
    }
}