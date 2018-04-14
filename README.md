# Simple Promise for Android

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

## Import

```
TODO
```

## Usage

The minimalistic Promise can be write such as :

```java
    new Promise<>(new PromiseSolver<String>() {
        @Override
        public void onCall(@NonNull Solver<String> solver) {
            //TODO implement your logic here
        }
    }).execute();
```

You can chain the promise with the following sample:

```java
        Promise<Boolean> promise = new Promise<>(new PromiseSolver<Boolean>() {
            @Override
            public void onCall(@NonNull Solver<Boolean> solver) {
                solver.resolve(false);
            }
        });

        //chain it
        promise
                .then(new PromiseExec<Boolean, String>() {
                    @Override
                    public void onCall(@Nullable Boolean result, Solver<String> solver) {
                        solver.resolve("previous result is := " + result);
                    }
                })
                .then(new PromiseExec<String, Integer>() {
                    @Override
                    public void onCall(@Nullable String result, Solver<Integer> solver) {
                        solver.resolve(result.length());
                    }
                })
                .then(new PromiseExec<Integer, Object>() {
                    @Override
                    public void onCall(@Nullable Integer result, Solver<Object> solver) {
                        //TODO something with the result which is the length of the appended false
                    }
                })
                .execute();
```

You can do whatever you want in the promise or the chained execution (Thread, Async post, Bugs, Save a cat)

## Resolve/Reject results calls

The Promises are using post to an Handler to manage the `resolve()` / `reject()` calls made to the `Solver`.

You can change the overall Handler using `Promise.setHandler(yourNonNullHandler)`

## execute (resolve) the Promise

Resolving a promise is as easy as calling `execute()` or manage `error(<ErrorPromise>)`

Once one of those 2 calls are made, the promise will start resolving itself.

Note that it is the best practice to manage the execution flow using `error(<ErrorPromise>)` to be sure
to grab any issues the Promise resolution could throw !


## Contributing

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change.

## License

This project is licensed under the GPL v3 License - see the [LICENSE](LICENSE) file for details