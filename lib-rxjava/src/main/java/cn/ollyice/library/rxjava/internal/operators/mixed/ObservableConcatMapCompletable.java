/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package cn.ollyice.library.rxjava.internal.operators.mixed;

import java.util.concurrent.atomic.*;

import cn.ollyice.library.rxjava.*;
import cn.ollyice.library.rxjava.annotations.Experimental;
import cn.ollyice.library.rxjava.disposables.Disposable;
import cn.ollyice.library.rxjava.exceptions.Exceptions;
import cn.ollyice.library.rxjava.functions.Function;
import cn.ollyice.library.rxjava.internal.disposables.DisposableHelper;
import cn.ollyice.library.rxjava.internal.functions.ObjectHelper;
import cn.ollyice.library.rxjava.internal.fuseable.*;
import cn.ollyice.library.rxjava.internal.queue.SpscLinkedArrayQueue;
import cn.ollyice.library.rxjava.internal.util.*;
import cn.ollyice.library.rxjava.plugins.RxJavaPlugins;

/**
 * Maps the upstream items into {@link CompletableSource}s and subscribes to them one after the
 * other completes or terminates (in error-delaying mode).
 * @param <T> the upstream value type
 * @since 2.1.11 - experimental
 */
@Experimental
public final class ObservableConcatMapCompletable<T> extends Completable {

    final Observable<T> source;

    final Function<? super T, ? extends CompletableSource> mapper;

    final ErrorMode errorMode;

    final int prefetch;

    public ObservableConcatMapCompletable(Observable<T> source,
            Function<? super T, ? extends CompletableSource> mapper,
            ErrorMode errorMode,
            int prefetch) {
        this.source = source;
        this.mapper = mapper;
        this.errorMode = errorMode;
        this.prefetch = prefetch;
    }

    @Override
    protected void subscribeActual(CompletableObserver s) {
        if (!ScalarXMapZHelper.tryAsCompletable(source, mapper, s)) {
            source.subscribe(new ConcatMapCompletableObserver<T>(s, mapper, errorMode, prefetch));
        }
    }

    static final class ConcatMapCompletableObserver<T>
    extends AtomicInteger
    implements Observer<T>, Disposable {

        private static final long serialVersionUID = 3610901111000061034L;

        final CompletableObserver downstream;

        final Function<? super T, ? extends CompletableSource> mapper;

        final ErrorMode errorMode;

        final AtomicThrowable errors;

        final ConcatMapInnerObserver inner;

        final int prefetch;

        SimpleQueue<T> queue;

        Disposable upstream;

        volatile boolean active;

        volatile boolean done;

        volatile boolean disposed;

        ConcatMapCompletableObserver(CompletableObserver downstream,
                Function<? super T, ? extends CompletableSource> mapper,
                ErrorMode errorMode, int prefetch) {
            this.downstream = downstream;
            this.mapper = mapper;
            this.errorMode = errorMode;
            this.prefetch = prefetch;
            this.errors = new AtomicThrowable();
            this.inner = new ConcatMapInnerObserver(this);
        }

        @Override
        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(upstream, s)) {
                this.upstream = s;
                if (s instanceof QueueDisposable) {
                    @SuppressWarnings("unchecked")
                    QueueDisposable<T> qd = (QueueDisposable<T>) s;

                    int m = qd.requestFusion(QueueDisposable.ANY);
                    if (m == QueueDisposable.SYNC) {
                        queue = qd;
                        done = true;
                        downstream.onSubscribe(this);
                        drain();
                        return;
                    }
                    if (m == QueueDisposable.ASYNC) {
                        queue = qd;
                        downstream.onSubscribe(this);
                        return;
                    }
                }
                queue = new SpscLinkedArrayQueue<T>(prefetch);
                downstream.onSubscribe(this);
            }
        }

        @Override
        public void onNext(T t) {
            if (t != null) {
                queue.offer(t);
            }
            drain();
        }

        @Override
        public void onError(Throwable t) {
            if (errors.addThrowable(t)) {
                if (errorMode == ErrorMode.IMMEDIATE) {
                    disposed = true;
                    inner.dispose();
                    t = errors.terminate();
                    if (t != ExceptionHelper.TERMINATED) {
                        downstream.onError(t);
                    }
                    if (getAndIncrement() == 0) {
                        queue.clear();
                    }
                } else {
                    done = true;
                    drain();
                }
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        @Override
        public void onComplete() {
            done = true;
            drain();
        }

        @Override
        public void dispose() {
            disposed = true;
            upstream.dispose();
            inner.dispose();
            if (getAndIncrement() == 0) {
                queue.clear();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        void innerError(Throwable ex) {
            if (errors.addThrowable(ex)) {
                if (errorMode == ErrorMode.IMMEDIATE) {
                    disposed = true;
                    upstream.dispose();
                    ex = errors.terminate();
                    if (ex != ExceptionHelper.TERMINATED) {
                        downstream.onError(ex);
                    }
                    if (getAndIncrement() == 0) {
                        queue.clear();
                    }
                } else {
                    active = false;
                    drain();
                }
            } else {
                RxJavaPlugins.onError(ex);
            }
        }

        void innerComplete() {
            active = false;
            drain();
        }

        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            AtomicThrowable errors = this.errors;
            ErrorMode errorMode = this.errorMode;

            do {
                if (disposed) {
                    queue.clear();
                    return;
                }

                if (!active) {

                    if (errorMode == ErrorMode.BOUNDARY) {
                        if (errors.get() != null) {
                            disposed = true;
                            queue.clear();
                            Throwable ex = errors.terminate();
                            downstream.onError(ex);
                            return;
                        }
                    }

                    boolean d = done;
                    boolean empty = true;
                    CompletableSource cs = null;
                    try {
                        T v = queue.poll();
                        if (v != null) {
                            cs = ObjectHelper.requireNonNull(mapper.apply(v), "The mapper returned a null CompletableSource");
                            empty = false;
                        }
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        disposed = true;
                        queue.clear();
                        upstream.dispose();
                        errors.addThrowable(ex);
                        ex = errors.terminate();
                        downstream.onError(ex);
                        return;
                    }

                    if (d && empty) {
                        disposed = true;
                        Throwable ex = errors.terminate();
                        if (ex != null) {
                            downstream.onError(ex);
                        } else {
                            downstream.onComplete();
                        }
                        return;
                    }

                    if (!empty) {
                        active = true;
                        cs.subscribe(inner);
                    }
                }
            } while (decrementAndGet() != 0);
        }

        static final class ConcatMapInnerObserver extends AtomicReference<Disposable>
        implements CompletableObserver {

            private static final long serialVersionUID = 5638352172918776687L;

            final ConcatMapCompletableObserver<?> parent;

            ConcatMapInnerObserver(ConcatMapCompletableObserver<?> parent) {
                this.parent = parent;
            }

            @Override
            public void onSubscribe(Disposable d) {
                DisposableHelper.replace(this, d);
            }

            @Override
            public void onError(Throwable e) {
                parent.innerError(e);
            }

            @Override
            public void onComplete() {
                parent.innerComplete();
            }

            void dispose() {
                DisposableHelper.dispose(this);
            }
        }
    }
}
