package io.machinecode.then.api;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A representation of a computation allowing listeners to be notified of state changes. This representation allows
 * for four main states: {@link #PENDING}, {@link #RESOLVED}, {@link #REJECTED} and {@link #CANCELLED}. Of these
 * {@link #RESOLVED}, {@link #REJECTED} and {@link #CANCELLED} will be referred to as 'terminal states', that is a
 * state when the {@link #isDone()} method will return {@code true}. A promise reaches one of these states by a call
 * to a 'terminal method', one of {@link #resolve(Object)}, {@link #reject(Throwable)} and {@link #cancel(boolean)}
 * respectively.
 *
 * The javadoc here only considers these three terminal states however this definition does not preclude inheritors
 * from adding further terminal states and associated methods.
 *
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 * @since 1.0
 */
public interface Promise<T, F extends Throwable> extends OnResolve<T>, OnReject<F>, OnCancel, Future<T> {

    byte PENDING   = 0;
    byte RESOLVED  = 1;
    byte REJECTED  = 2;
    byte CANCELLED = 3;

    /**
     * Called to indicate the successful completion of the computation this promise represents. After this method has
     * been called {@link #isDone()} will return {@code true}. If this was the first terminal method to be called
     * {@link #isResolved()} will also return {@code true}.
     *
     * @param that The result of the computation.
     * @throws ListenerException May be thrown if a listener throws an exception.
     *                           It is not required that this be thrown in this instance.
     * @throws ResolvedException May be thrown by an implementation if resolve has previously called.
     *                           It is not required that this be thrown in this instance.
     * @throws RejectedException May be thrown by an implementation if {@link #reject(Throwable)} has previously called.
     *                           It is not required that this be thrown in this instance.
     * @throws CancelledException May be thrown by an implementation if {@link #cancel(boolean)} has previously called.
     *                            It is not required that this be thrown in this instance.
     */
    @Override
    void resolve(final T that) throws ListenerException, ResolvedException, RejectedException, CancelledException;

    /**
     * Called to indicate the failure of the computation this promise represents. After this method has
     * been called {@link #isDone()} will return {@code true}. If this was the first terminal method to be called
     * {@link #isRejected()} will also return {@code true}.
     *
     * @param that The exception that caused the computation to terminate.
     * @throws ListenerException May be thrown if a listener throws an exception.
     *                           It is not required that this be thrown in this instance.
     * @throws ResolvedException May be thrown by an implementation if {@link #resolve(Object)} has previously called.
     *                           It is not required that this be thrown in this instance.
     * @throws RejectedException May be thrown by an implementation if reject has previously called.
     *                           It is not required that this be thrown in this instance.
     * @throws CancelledException May be thrown by an implementation if {@link #cancel(boolean)} has previously called.
     *                           It is not required that this be thrown in this instance.
     */
    @Override
    void reject(final F that) throws ListenerException, ResolvedException, RejectedException, CancelledException;

    /**
     * Called to attempt to stop the computation. Calling this method does not guarantee that the computation will
     * cease however if this method is the first terminal method called, an implementation must guarantee that any
     * listeners will not by notified of calls to any other terminal methods. After this method has
     * been called {@link #isDone()} will return {@code true}. If this was the first terminal method to be called
     * {@link #isCancelled()} will also return {@code true}.
     *
     * @throws ListenerException May be thrown if a listener throws an exception.  It is not required that this be
     *                           thrown in this instance.
     * @param interrupt {@code true} If the computation should be interrupted in the case that it has already commenced.
     * @return {@code true} If the promise was cancelled, {@code false} if it had already reached another terminal state.
     * @see Future#cancel(boolean)
     */
    @Override
    boolean cancel(final boolean interrupt) throws ListenerException;

    /**
     * @return {@code true} if any terminal method has been called.
     */
    @Override
    boolean isDone();

    /**
     * @return {@code true} if {@link #resolve(Object)} was the first terminal method called.
     */
    boolean isResolved();

    /**
     * @return {@code true} if {@link #reject(Throwable)} was  the first terminal method called.
     */
    boolean isRejected();

    /**
     * @return {@code true} if {@link #cancel(boolean)} was the first terminal method called.
     */
    @Override
    boolean isCancelled();

    /**
     * Triggered on any event after which {@link #isResolved()} will return {@code true}.
     *
     * @param then Callback to be executed
     * @return This instance for method chaining.
     */
    Promise<T,F> onResolve(final OnResolve<T> then);

    /**
     * Triggered on any event after which {@link #isRejected()} will return {@code true}.
     *
     * @param then Callback to be executed
     * @return This instance for method chaining.
     */
    Promise<T,F> onReject(final OnReject<F> then);

    /**
     * Triggered on any event after which {@link #isCancelled()} will return {@code true}.
     *
     * @param then Callback to be executed
     * @return This instance for method chaining.
     */
    Promise<T,F> onCancel(final OnCancel then);

    /**
     * Triggered on any event after which {@link #isDone()} will return {@code true};
     * Will be fired in addition to the callback for the specific event.
     *
     * @param then Callback to be executed
     * @return This instance for method chaining.
     */
    Promise<T,F> onComplete(final OnComplete then);

    /**
     * Triggered when {@link #get(long, TimeUnit)} or {@link #get()} is called.
     * It will be called after this promise has transitioned into a state
     * where {@link #isDone()} will return {@code true}.
     *
     * Each get method will call the corresponding get method on the {@link Future}
     * in the thread that called either {@link Promise#get()} or {@link Promise#get(long, TimeUnit)}.
     *
     * @param then Callback to be executed
     * @return This instance for method chaining.
     */
    Promise<T,F> onGet(final Future<?> then);
}
