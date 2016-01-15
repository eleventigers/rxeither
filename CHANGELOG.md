Change Log
==========

Version 1.1.0 *(15-01-2016)*
----------------------------
* Added lazy fold [`Action1<Either<L, R>`](https://github.com/eleventigers/rxeither/blob/88546a2173f6ee0c4667ba7c43d1c1b8abd18f1c/rxeither/src/main/java/net/jokubasdargis/rxeither/RxEither.java#L64)
and [`Func1<Either<L, R>, T>`](https://github.com/eleventigers/rxeither/blob/88546a2173f6ee0c4667ba7c43d1c1b8abd18f1c/rxeither/src/main/java/net/jokubasdargis/rxeither/RxEither.java#L71)
variants to the RxEither.
* Exposed [`isLeft`](https://github.com/eleventigers/rxeither/blob/88546a2173f6ee0c4667ba7c43d1c1b8abd18f1c/rxeither/src/main/java/net/jokubasdargis/rxeither/RxEither.java#L36)
and [`isRight`](https://github.com/eleventigers/rxeither/blob/88546a2173f6ee0c4667ba7c43d1c1b8abd18f1c/rxeither/src/main/java/net/jokubasdargis/rxeither/RxEither.java#L43) filtering functions from RxEither.

Version 1.0.1 *(13-01-2016)*
----------------------------

Fixed 1.7 source compatibility issue.

Version 1.0.0 *(13-01-2016)*
----------------------------

Initial release.