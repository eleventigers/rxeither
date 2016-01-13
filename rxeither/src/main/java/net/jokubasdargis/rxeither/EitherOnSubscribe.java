package net.jokubasdargis.rxeither;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class EitherOnSubscribe<L, R> implements Observable.OnSubscribe<Either<L, R>> {

    private final Observable<L> left;
    private final Observable<R> right;

    EitherOnSubscribe(Observable<L> left, Observable<R> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void call(final Subscriber<? super Either<L, R>> subscriber) {
        final AtomicBoolean done = new AtomicBoolean();

        final Subscription leftSubscription = left.subscribe(new Observer<L>() {
            @Override
            public void onCompleted() {
                if (done.compareAndSet(false, true)) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (done.compareAndSet(false, true)) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }

            @Override
            public void onNext(L l) {
                if (!done.get()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(Either.<L, R>left(l));
                    }
                }
            }
        });

        final Subscription rightSubscription = right.subscribe(new Observer<R>() {
            @Override
            public void onCompleted() {
                if (done.compareAndSet(false, true)) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (done.compareAndSet(false, true)) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }

            @Override
            public void onNext(R r) {
                if (!done.get()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(Either.<L, R>right(r));
                    }
                }
            }
        });

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                leftSubscription.unsubscribe();
                rightSubscription.unsubscribe();
            }
        }));
    }
}
