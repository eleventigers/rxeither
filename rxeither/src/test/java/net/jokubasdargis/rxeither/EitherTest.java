package net.jokubasdargis.rxeither;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import rx.functions.Action1;
import rx.functions.Func1;

@SuppressWarnings("unchecked")
public final class EitherTest {

    private final EventA eventA = new EventA();
    private final EventB eventB = new EventB();
    private final Func1<EventA, EventA> funcAA = mock(Func1.class);
    private final Func1<EventB, EventA> funcBA = mock(Func1.class);
    private final Func1<EventA, EventB> funcAB = mock(Func1.class);
    private final Func1<EventB, EventB> funcBB = mock(Func1.class);
    private final Action1<EventA> actionA = mock(Action1.class);
    private final Action1<EventB> actionB = mock(Action1.class);

    @Test
    public void foldLeftFunc() {
        Either<EventA, EventB> left = Either.left(eventA);

        left.fold(funcAA, funcBA);

        verify(funcAA).call(eventA);
        verifyNoMoreInteractions(funcBA);
    }

    @Test
    public void foldLeftAction() {
        Either<EventA, EventB> left = Either.left(eventA);

        left.fold(actionA, actionB);

        verify(actionA).call(eventA);
        verifyNoMoreInteractions(actionB);
    }

    @Test
    public void foldRightFunc() {
        Either<EventA, EventB> right = Either.right(eventB);

        right.fold(funcAB, funcBB);

        verify(funcBB).call(eventB);
        verifyNoMoreInteractions(funcAB);
    }

    @Test
    public void foldRightAction() {
        Either<EventA, EventB> right = Either.right(eventB);

        right.fold(actionA, actionB);

        verify(actionB).call(eventB);
        verifyNoMoreInteractions(actionA);
    }

    @Test
    public void leftEquals() {
        Either<EventA, EventB> left1 = Either.left(eventA);
        Either<EventA, EventB> left2 = Either.left(eventA);

        assertThat(left1).isEqualTo(left2);
    }

    @Test
    public void rightEquals() {
        Either<EventA, EventB> right1 = Either.right(eventB);
        Either<EventA, EventB> right2 = Either.right(eventB);

        assertThat(right1).isEqualTo(right2);
    }

    @Test
    public void leftIsLeft() {
        Either<EventA, EventB> left = Either.left(eventA);

        assertThat(left.isLeft()).isTrue();
    }

    @Test
    public void leftIsNotRight() {
        Either<EventA, EventB> left = Either.left(eventA);

        assertThat(left.isRight()).isFalse();
    }

    @Test
    public void rightIsRight() {
        Either<EventA, EventB> right = Either.right(eventB);

        assertThat(right.isRight()).isTrue();
    }

    @Test
    public void rightIsNotLeft() {
        Either<EventA, EventB> right = Either.right(eventB);

        assertThat(right.isLeft()).isFalse();
    }
}
