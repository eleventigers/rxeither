package net.jokubasdargis.rxeither;

import com.pacoworks.rxsealedunions.Union2;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Represents a value of one of two possible types (a disjoint union.) Instances are either
 * instance of {@link Left} or {@link Right}.
 *
 * @param <L> The type of left value.
 * @param <R> The type of right value.
 */
public abstract class Either<L, R> implements Union2<L, R> {

    /**
     * Creates {@link Either} instance for the given value as left.
     */
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    /**
     * Creates {@link Either} instance for the given value as right.
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    Either() { }

    /**
     * @return true if this is a Left, false otherwise.
     */
    public abstract boolean isLeft();

    /**
     * @return false if this is a Right, false otherwise.
     */
    public abstract boolean isRight();

    /**
     * @deprecated - use {@link #continued(Action1, Action1)} instead.
     * <p>
     * Applies left {@link Action1} if this is a Left or right {@link Action1} if this is a Right.
     */
    @Deprecated
    public abstract void fold(Action1<L> left, Action1<R> right);

    /**
     * @deprecated - use {@link #join(Func1, Func1)}} instead.
     * <p>
     * Applies left {@link Func1} if this is a Left or right {@link Func1} if this is a Right.
     */
    @Deprecated
    public abstract <T> T fold(Func1<L, T> left, Func1<R, T> right);
}
