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
     * Checks whether {@link Either} is left.
     */
    public static <L, R> Func1<Either<L, R>, Boolean> isLeft() {
        return IsLeft.instance();
    }

    /**
     * Checks whether {@link Either} is right.
     */
    public static <L, R> Func1<Either<L, R>, Boolean> isRight() {
        return IsRight.instance();
    }

    /**
     * Filters left side of {@link Either} observable.
     */
    public static <L, R> Observable<L> filterLeft(Observable<Either<L, R>> either) {
        return either.filter(RxEither.<L, R>isLeft()).map(FoldLeft.<L, R>instance());
    }

    /**
     * Filters right side of {@link Either} observable.
     */
    public static <L, R> Observable<R> filterRight(Observable<Either<L, R>> either) {
        return either.filter(RxEither.<L, R>isRight()).map(FoldRight.<L, R>instance());
    }

    private static class FoldLeft<L, R> implements Func1<Either<L, R>, L> {

        @SuppressWarnings("unchecked")
        static <L, R> FoldLeft<L, R> instance() {
            return (FoldLeft<L, R>) Holder.INSTANCE;
        }

        @Override
        public L call(Either<L, R> lrEither) {
            return lrEither.fold(Identity.<L>instance(), Nothing.<R, L>instance());
        }

        private static class Holder {

            static final FoldLeft<?, ?> INSTANCE = new FoldLeft<>();
        }
    }

    private static class FoldRight<L, R> implements Func1<Either<L, R>, R> {

        @SuppressWarnings("unchecked")
        static <L, R> FoldRight<L, R> instance() {
            return (FoldRight<L, R>) Holder.INSTANCE;
        }

        @Override
        public R call(Either<L, R> lrEither) {
            return lrEither.fold(Nothing.<L, R>instance(), Identity.<R>instance());
        }

        private static class Holder {

            static final FoldRight<?, ?> INSTANCE = new FoldRight<>();
        }
    }

    private static class IsLeft<L, R> implements Func1<Either<L, R>, Boolean> {

        @SuppressWarnings("unchecked")
        static <L, R> IsLeft<L, R> instance() {
            return (IsLeft<L, R>) Holder.INSTANCE;
        }

        @Override
        public Boolean call(Either<L, R> lrEither) {
            return lrEither.isLeft();
        }

        private static class Holder {

            static final IsLeft<?, ?> INSTANCE = new IsLeft<>();
        }
    }

    private static class IsRight<L, R> implements Func1<Either<L, R>, Boolean> {

        @SuppressWarnings("unchecked")
        static <L, R> IsRight<L, R> instance() {
            return (IsRight<L, R>) Holder.INSTANCE;
        }

        @Override
        public Boolean call(Either<L, R> lrEither) {
            return lrEither.isRight();
        }

        private static class Holder {

            static final IsRight<?, ?> INSTANCE = new IsRight<>();
        }
    }

    private static class Nothing<T, R> implements Func1<T, R> {

        @SuppressWarnings("unchecked")
        static <T, R> Nothing<T, R> instance() {
            return (Nothing<T, R>) Holder.INSTANCE;
        }

        @Override
        public R call(T t) {
            return null;
        }

        private static class Holder {

            static final Nothing<?, ?> INSTANCE = new Nothing<>();
        }
    }

    private static class Identity<T> implements Func1<T, T> {

        @SuppressWarnings("unchecked")
        static <T> Identity<T> instance() {
            return (Identity<T>) Holder.INSTANCE;
        }

        @Override
        public T call(T t) {
            return t;
        }

        private static class Holder {

            static final Identity<?> INSTANCE = new Identity<>();
        }
    }

    private RxEither() {
        throw new AssertionError("No instances");
    }
}
