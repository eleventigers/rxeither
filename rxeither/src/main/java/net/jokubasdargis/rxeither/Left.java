package net.jokubasdargis.rxeither;

import rx.functions.Action1;
import rx.functions.Func1;

final class Left<L, R> extends Either<L, R> {

    private final L value;

    Left(L value) {
        this.value = value;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public void fold(Action1<L> left, Action1<R> right) {
        left.call(value);
    }

    @Override
    public <T> T fold(Func1<L, T> left, Func1<R, T> right) {
        return left.call(value);
    }

    @Override
    public String toString() {
        return "Left{" + "value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Left) {
            Left<?, ?> that = (Left<?, ?>) o;
            return (this.value.equals(that.value));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.value.hashCode();
        return h;
    }
}
