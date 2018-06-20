package cn.ollyice.library.retrofit.mock;


import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

import cn.ollyice.library.okhttp.Request;
import cn.ollyice.library.retrofit.Call;
import cn.ollyice.library.retrofit.Callback;
import cn.ollyice.library.retrofit.Response;

/** Factory methods for creating {@link Call} instances which immediately respond or fail. */
public final class Calls {
    /**
     * Invokes {@code callable} once for the returned {@link Call} and once for each instance that is
     * obtained from {@linkplain Call#clone() cloning} the returned {@link Call}.
     */
    public static <T> Call<T> defer(Callable<Call<T>> callable) {
        return new DeferredCall<>(callable);
    }

    public static <T> Call<T> response(@Nullable T successValue) {
        return new FakeCall<>(Response.success(successValue), null);
    }

    public static <T> Call<T> response(Response<T> response) {
        return new FakeCall<>(response, null);
    }

    /** Creates a failed {@link Call} from {@code failure}. */
    public static <T> Call<T> failure(IOException failure) {
        // TODO delete this overload in Retrofit 3.0.
        return new FakeCall<>(null, failure);
    }

    /**
     * Creates a failed {@link Call} from {@code failure}.
     * <p>
     * Note: When invoking {@link Call#execute() execute()} on the returned {@link Call}, if
     * {@code failure} is a {@link RuntimeException}, {@link Error}, or {@link IOException} subtype
     * it is thrown directly. Otherwise it is "sneaky thrown" despite not being declared.
     */
    public static <T> Call<T> failure(Throwable failure) {
        return new FakeCall<>(null, failure);
    }

    private Calls() {
        throw new AssertionError("No instances.");
    }

    static final class FakeCall<T> implements Call<T> {
        private final Response<T> response;
        private final Throwable error;
        private final AtomicBoolean canceled = new AtomicBoolean();
        private final AtomicBoolean executed = new AtomicBoolean();

        FakeCall(@Nullable Response<T> response, @Nullable Throwable error) {
            if ((response == null) == (error == null)) {
                throw new AssertionError("Only one of response or error can be set.");
            }
            this.response = response;
            this.error = error;
        }

        @Override public Response<T> execute() throws IOException {
            if (!executed.compareAndSet(false, true)) {
                throw new IllegalStateException("Already executed");
            }
            if (canceled.get()) {
                throw new IOException("canceled");
            }
            if (response != null) {
                return response;
            }
            throw FakeCall.<Error>sneakyThrow2(error);
        }

        @SuppressWarnings("unchecked") // Intentionally abusing this feature.
        private static <T extends Throwable> T sneakyThrow2(Throwable t) throws T {
            throw (T) t;
        }

        @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
        @Override public void enqueue(Callback<T> callback) {
            if (callback == null) {
                throw new NullPointerException("callback == null");
            }
            if (!executed.compareAndSet(false, true)) {
                throw new IllegalStateException("Already executed");
            }
            if (canceled.get()) {
                callback.onFailure(this, new IOException("canceled"));
            } else if (response != null) {
                callback.onResponse(this, response);
            } else {
                callback.onFailure(this, error);
            }
        }

        @Override public boolean isExecuted() {
            return executed.get();
        }

        @Override public void cancel() {
            canceled.set(true);
        }

        @Override public boolean isCanceled() {
            return canceled.get();
        }

        @Override public Call<T> clone() {
            return new FakeCall<>(response, error);
        }

        @Override public Request request() {
            if (response != null) {
                return response.raw().request();
            }
            return new Request.Builder().url("http://localhost").build();
        }
    }

    static final class DeferredCall<T> implements Call<T> {
        private final Callable<Call<T>> callable;
        private Call<T> delegate;

        DeferredCall(Callable<Call<T>> callable) {
            this.callable = callable;
        }

        private synchronized Call<T> getDelegate() {
            Call<T> delegate = this.delegate;
            if (delegate == null) {
                try {
                    delegate = callable.call();
                } catch (Exception e) {
                    delegate = failure(e);
                }
                this.delegate = delegate;
            }
            return delegate;
        }

        @Override public Response<T> execute() throws IOException {
            return getDelegate().execute();
        }

        @Override public void enqueue(Callback<T> callback) {
            getDelegate().enqueue(callback);
        }

        @Override public boolean isExecuted() {
            return getDelegate().isExecuted();
        }

        @Override public void cancel() {
            getDelegate().cancel();
        }

        @Override public boolean isCanceled() {
            return getDelegate().isCanceled();
        }

        @Override public Call<T> clone() {
            return new DeferredCall<>(callable);
        }

        @Override public Request request() {
            return getDelegate().request();
        }
    }
}