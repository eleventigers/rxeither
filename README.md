RxEither [![Build Status](https://travis-ci.org/eleventigers/rxeither.svg?branch=master)](https://travis-ci.org/eleventigers/rxeither)
========

Either is a value of one of two possible types.

Inspired by Jeffrey van Gogh's [discussion][jeffrey] with the Rx.Net users, RxEither is a port of Scala's [Either][either] for the [RxJava][rx] world.

Usage
-----

* **Progress reporting**

  ```java
  public class ProgressExample {

       public static void main(String[] args) {
              PublishSubject<Integer> progress = PublishSubject.create();

              Observable<String> results = Observable.fromCallable(() -> {
                  progress.onNext(0);
                  TimeUnit.SECONDS.sleep(1);
                  progress.onNext(100);
                  return "Hello, world!";
              }).subscribeOn(Schedulers.io());

              Observable<Either<Integer, String>> either = RxEither.from(progress, results).share();

              RxEither.filterLeft(either).subscribe(System.out::println);
              RxEither.filterRight(either).toBlocking().subscribe(System.out::println);
          }
  }
  ```
  Prints:

  ```
  0
  100
  Hello, world!
  ```

Download
--------
Maven:
```xml
<dependency>
  <groupId>net.jokubasdargis.rxeither</groupId>
  <artifactId>rxeither</artifactId>
  <version>1.0.1</version>
</dependency>
```
Gradle:
```groovy
compile 'net.jokubasdargis.rxeither:rxeither:1.0.1'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

License
-------

    Copyright 2016 Jokubas Dargis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [either]: http://www.scala-lang.org/api/rc2/scala/Either.html
 [rx]: https://github.com/ReactiveX/RxJava/
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
 [jeffrey]: https://social.msdn.microsoft.com/Forums/en-US/abe175c9-fad6-4d9c-b3e1-012a14f96fda/exposing-progress-vs-data