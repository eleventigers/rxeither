package net.jokubasdargis.rxeither.example;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import net.jokubasdargis.rxeither.Either;
import net.jokubasdargis.rxeither.RxEither;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class ProgressExample {

    public static void main(String[] args) {
        final PublishSubject<Integer> progress = PublishSubject.create();

        Observable<String> results = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                progress.onNext(0);
                TimeUnit.SECONDS.sleep(1);
                progress.onNext(100);
                return "Hello, world!";
            }
        }).subscribeOn(Schedulers.io());

        Observable<Either<Integer, String>> either = RxEither.from(progress, results).share();

        RxEither.filterLeft(either).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("Progress: " + integer);
            }
        });
        RxEither.filterRight(either).toBlocking().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("Results: " + s);
            }
        });
    }
}

