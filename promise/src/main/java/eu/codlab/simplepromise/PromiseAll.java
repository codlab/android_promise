package eu.codlab.simplepromise;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import eu.codlab.simplepromise.solve.ErrorPromise;
import eu.codlab.simplepromise.solve.PromiseExec;
import eu.codlab.simplepromise.solve.PromiseSolver;
import eu.codlab.simplepromise.solve.Solver;

/**
 * Created by kevinleperf on 15/04/2018.
 */

class PromiseAll<TYPE_EXECUTE> extends AbstractPromiseMulti<TYPE_EXECUTE> {

    private AtomicInteger mDone = new AtomicInteger(0);
    private AtomicBoolean mSent = new AtomicBoolean(false);
    private boolean mIsRejected = false;
    private Solver<List<TYPE_EXECUTE>> mSolver;
    private Object[] mResults;


    public PromiseAll(AbstractPromise<TYPE_EXECUTE>... promises) {
        super(promises);
    }

    public Promise<List<TYPE_EXECUTE>> all() {
        return new Promise<>(new PromiseSolver<List<TYPE_EXECUTE>>() {
            @Override
            public void onCall(@NonNull final Solver<List<TYPE_EXECUTE>> final_solver) {
                mResults = new Object[getPromises().size()];
                mSolver = final_solver;
                int index = 0;

                for (AbstractPromise<TYPE_EXECUTE> promise : getPromises()) {
                    final int current_index = index++;
                    promise
                            .then(new PromiseExec<TYPE_EXECUTE, Void>() {
                                @Override
                                public void onCall(@Nullable TYPE_EXECUTE result, @NonNull Solver<Void> solver) {
                                    System.out.println("executing for " + current_index);
                                    if (!mIsRejected) {
                                        mResults[current_index] = result;
                                        mDone.incrementAndGet();

                                        if (!mSent.get() && mDone.get() == getPromises().size()) {
                                            mSent.set(true);
                                            onDone();
                                        }
                                    }
                                }
                            })
                            .error(new ErrorPromise() {
                                @Override
                                public void onError(@NonNull Throwable error) {
                                    if (!mSent.get() && !mIsRejected) {
                                        mSent.set(true);
                                        mIsRejected = true;
                                        mSolver.reject(error);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void onDone() {
        List<TYPE_EXECUTE> list = new ArrayList<>();
        for (Object object : mResults) list.add((TYPE_EXECUTE) object);
        mSolver.resolve(list);
    }
}
