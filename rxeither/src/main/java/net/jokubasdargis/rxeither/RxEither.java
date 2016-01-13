package net.jokubasdargis.rxeither;

import rx.Observable;
import rx.functions.Func1;

/**
 * Helper to create and filter {@link Observable}s of {@link Either} type.
 */
public final class RxEither {

    /**
     * Creates only left {@link Either} type emitting observable.
     */
    public static <L, R> Observable<Either<L, R>> left(Observable<L> left) {
        return from(left, Observable.<R>never());
    }

    /**
     * Creates only right {@link Either} type emitting observable.
     */
    public static <L, R> Observable<Either<L, R>> right(Observable<R> right) {
        return from(Observable.<L>never(), right);
    }

    /**
     * Combines two observables into a single {@link Either} observable.
     */
    public static <L, R> Observable<Either<L, R>> from(Observable<L> left, Observable<R> right) {
        return Observable.create(new EitherOnSubscribe<>(left, right));
    }

    /**
     * Filters left side of {@link Either} observable.
     */
    public static <L, R> Observable<L> filterLeft(Observable<Either<L, R>> either) {
        return either.map(new Func1<Either<L, R>, L>() {
            @Override
            public L call(Either<L, R> lrEither) {
                return lrEither.fold(new Func1<L, L>() {
                    @Override
                    public L call(L l) {
                        return l;
                    }
                }, new Func1<R, L>() {
                    @Override
                    public L call(R r) {
                        return null;
                    }
                });
            }
        }).filter(notNull());
    }

    /**
     * Filters right side of {@link Either} observable.
     */
    public static <L, R> Observable<R> filterRight(Observable<Either<L, R>> either) {
        return either.map(new Func1<Either<L, R>, R>() {
            @Override
            public R call(Either<L, R> lrEither) {
                return lrEither.fold(new Func1<L, R>() {
                    @Override
                    public R call(L l) {
                        return null;
                    }
                }, new Func1<R, R>() {
                    @Override
                    public R call(R r) {
                        return r;
                    }
                });
            }
        }).filter(notNull());
    }

    private static <T> Func1<T, Boolean> notNull() {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                return t != null;
            }
        };
    }

    private RxEither() {
        throw new AssertionError("No instances");
    }
}
