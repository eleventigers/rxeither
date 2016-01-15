package net.jokubasdargis.rxeither;

import rx.Observable;
import rx.functions.Action1;
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

    /**
     * Creates an {@link Action1} to lazily invoke the provided fold {@link Action1}s.
     */
    public static <L, R> Action1<Either<L, R>> foldLazy(Action1<L> left, Action1<R> right) {
        return FoldLazyAction.create(left, right);
    }

    /**
     * Creates a {@link Func1} to lazily get a fold result from the provided {@link Func1}s.
     */
    public static <L, R, T> Func1<Either<L, R>, T> foldLazy(Func1<L, T> left, Func1<R, T> right) {
        return FoldLazyFunc.create(left, right);
    }

    private static class FoldLazyAction<L, R> implements Action1<Either<L, R>> {

        private final Action1<L> left;
        private final Action1<R> right;

        static <L, R> Action1<Either<L, R>> create(Action1<L> left, Action1<R> right) {
            return new FoldLazyAction<>(left, right);
        }

        @Override
        public void call(Either<L, R> lrEither) {
            lrEither.fold(left, right);
        }

        private FoldLazyAction(Action1<L> left, Action1<R> right) {
            this.left = left;
            this.right = right;
        }
    }

    private static class FoldLazyFunc<L, R, T> implements Func1<Either<L, R>, T> {

        private final Func1<L, T> left;
        private final Func1<R, T> right;

        public static <L, R, T> Func1<Either<L, R>, T> create(Func1<L, T> left, Func1<R, T> right) {
            return new FoldLazyFunc<>(left, right);
        }

        @Override
        public T call(Either<L, R> lrEither) {
            return lrEither.fold(left, right);
        }

        private FoldLazyFunc(Func1<L, T> left, Func1<R, T> right) {
            this.left = left;
            this.right = right;
        }
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
