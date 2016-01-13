package net.jokubasdargis.rxeither;

import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.subjects.TestSubject;

public final class RxEitherTest {

    private final EventA eventA = new EventA();
    private final EventB eventB = new EventB();
    private final TestScheduler testScheduler = Schedulers.test();
    private final TestSubscriber<Either<EventA, EventB>> subscriber = new TestSubscriber<>();
    private final TestSubject<EventA> eventASubject = TestSubject.create(testScheduler);
    private final TestSubject<EventB> eventBSubject = TestSubject.create(testScheduler);

    @Test
    public void singleLeft() {
        Observable<Either<EventA, EventB>> either = RxEither.left(eventASubject);
        either.subscribe(subscriber);

        eventASubject.onNext(eventA);

        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertValue(Either.<EventA, EventB>left(eventA));
    }

    @Test
    public void singleRight() {
        Observable<Either<EventA, EventB>> either = RxEither.right(eventBSubject);
        either.subscribe(subscriber);

        eventBSubject.onNext(eventB);

        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertValue(Either.<EventA, EventB>right(eventB));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multipleLeftRight() {
        Observable<Either<EventA, EventB>> either = RxEither.from(eventASubject, eventBSubject);
        either.subscribe(subscriber);

        eventASubject.onNext(eventA);
        eventBSubject.onNext(eventB);
        eventBSubject.onNext(eventB);
        eventASubject.onNext(eventA);

        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertValues(Either.<EventA, EventB>left(eventA),
                                Either.<EventA, EventB>right(eventB),
                                Either.<EventA, EventB>right(eventB),
                                Either.<EventA, EventB>left(eventA));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void singleLeftTerminalRight() {
        Observable<Either<EventA, EventB>> either = RxEither.from(eventASubject, eventBSubject);
        either.subscribe(subscriber);

        eventASubject.onNext(eventA);
        eventBSubject.onNext(eventB);
        eventBSubject.onCompleted();
        eventASubject.onNext(eventA);

        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        subscriber.assertUnsubscribed();
        subscriber.assertValues(Either.<EventA, EventB>left(eventA),
                                Either.<EventA, EventB>right(eventB));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void multipleLeftTerminalRightOtherThread() {
        TestScheduler otherScheduler = Schedulers.test();
        TestSubject<EventB> eventBSubject = TestSubject.create(otherScheduler);
        Observable<Either<EventA, EventB>> either = RxEither.from(eventASubject, eventBSubject);
        either.subscribe(subscriber);

        eventASubject.onNext(eventA);
        eventBSubject.onNext(eventB);
        eventBSubject.onCompleted();
        eventASubject.onNext(eventA);

        testScheduler.triggerActions();
        subscriber.assertNotCompleted();
        otherScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        subscriber.assertUnsubscribed();
        subscriber.assertValues(Either.<EventA, EventB>left(eventA),
                                Either.<EventA, EventB>left(eventA),
                                Either.<EventA, EventB>right(eventB));
    }

    @Test
    public void errorLeftBlockRight() {
        Throwable error = new Throwable();
        Observable<Either<EventA, EventB>> either = RxEither.from(eventASubject, eventBSubject);
        either.subscribe(subscriber);

        eventASubject.onError(error);
        eventBSubject.onNext(eventB);

        testScheduler.triggerActions();

        subscriber.assertError(error);
        subscriber.assertNotCompleted();
        subscriber.assertUnsubscribed();
        subscriber.assertNoValues();
    }

    @Test
    public void filterLeft() {
        TestSubscriber<EventA> subscriber = new TestSubscriber<>();
        Observable<EventA> left = RxEither.filterLeft(RxEither.from(eventASubject, eventBSubject));
        left.subscribe(subscriber);

        eventASubject.onNext(eventA);
        eventBSubject.onNext(eventB);
        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertValue(eventA);
    }

    @Test
    public void filterRight() {
        TestSubscriber<EventB> subscriber = new TestSubscriber<>();
        Observable<EventB> right =
                RxEither.filterRight(RxEither.from(eventASubject, eventBSubject));
        right.subscribe(subscriber);

        eventASubject.onNext(eventA);
        eventBSubject.onNext(eventB);
        testScheduler.triggerActions();

        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertValue(eventB);
    }
}
