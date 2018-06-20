package cn.ollyice.library.retrofit.mock;


import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import cn.ollyice.library.okhttp.Request;
import cn.ollyice.library.retrofit.Call;
import cn.ollyice.library.retrofit.Callback;
import cn.ollyice.library.retrofit.Response;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

final class BehaviorCall<T> implements Call<T> {
    final NetworkBehavior behavior;
    final ExecutorService backgroundExecutor;
    final Call<T> delegate;

    private volatile Future<?> task;
    volatile boolean canceled;
    private boolean executed;

    BehaviorCall(NetworkBehavior behavior, ExecutorService backgroundExecutor, Call<T> delegate) {
        this.behavior = behavior;
        this.backgroundExecutor = backgroundExecutor;
        this.delegate = delegate;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone") // We are a final type & this saves clearing state.
    @Override public Call<T> clone() {
        return new BehaviorCall<>(behavior, backgroundExecutor, delegate.clone());
    }

    @Override public Request request() {
        return delegate.request();
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    @Override public void enqueue(final Callback<T> callback) {
        if (callback == null) throw new NullPointerException("callback == null");

        synchronized (this) {
            if (executed) throw new IllegalStateException("Already executed");
            executed = true;
        }
        task = backgroundExecutor.submit(new Runnable() {
            boolean delaySleep() {
                long sleepMs = behavior.calculateDelay(MILLISECONDS);
                if (sleepMs > 0) {
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        callback.onFailure(BehaviorCall.this, new IOException("canceled"));
                        return false;
                    }
                }
                return true;
            }

            @Override public void run() {
                if (canceled) {
                    callback.onFailure(BehaviorCall.this, new IOException("canceled"));
                } else if (behavior.calculateIsFailure()) {
                    if (delaySleep()) {
                        callback.onFailure(BehaviorCall.this, behavior.failureException());
                    }
                } else if (behavior.calculateIsError()) {
                    if (delaySleep()) {
                        //noinspection unchecked An error response has no body.
                        callback.onResponse(BehaviorCall.this, (Response<T>) behavior.createErrorResponse());
                    }
                } else {
                    delegate.enqueue(new Callback<T>() {
                        @Override public void onResponse(Call<T> call, Response<T> response) {
                            if (delaySleep()) {
                                callback.onResponse(call, response);
                            }
                        }

                        @Override public void onFailure(Call<T> call, Throwable t) {
                            if (delaySleep()) {
                                callback.onFailure(call, t);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override public synchronized boolean isExecuted() {
        return executed;
    }

    @Override public Response<T> execute() throws IOException {
        final AtomicReference<Response<T>> responseRef = new AtomicReference<>();
        final AtomicReference<Throwable> failureRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        enqueue(new Callback<T>() {
            @Override public void onResponse(Call<T> call, Response<T> response) {
                responseRef.set(response);
                latch.countDown();
            }

            @Override public void onFailure(Call<T> call, Throwable t) {
                failureRef.set(t);
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IOException("canceled");
        }
        Response<T> response = responseRef.get();
        if (response != null) return response;
        Throwable failure = failureRef.get();
        if (failure instanceof RuntimeException) throw (RuntimeException) failure;
        if (failure instanceof IOException) throw (IOException) failure;
        throw new RuntimeException(failure);
    }

    @Override public void cancel() {
        canceled = true;
        Future<?> task = this.task;
        if (task != null) {
            task.cancel(true);
        }
    }

    @Override public boolean isCanceled() {
        return canceled;
    }
}