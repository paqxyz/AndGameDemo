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

package cn.ollyice.library.rxjava.internal.operators.completable;

import java.util.concurrent.atomic.AtomicInteger;

import cn.ollyice.library.rxjava.*;
import cn.ollyice.library.rxjava.annotations.Experimental;
import cn.ollyice.library.rxjava.disposables.Disposable;
import cn.ollyice.library.rxjava.exceptions.Exceptions;
import cn.ollyice.library.rxjava.functions.Action;
import cn.ollyice.library.rxjava.internal.disposables.DisposableHelper;
import cn.ollyice.library.rxjava.plugins.RxJavaPlugins;

/**
 * Execute an action after an onError, onComplete or a dispose event.
 *
 * @since 2.0.1 - experimental
 */
@Experimental
public final class CompletableDoFinally extends Completable {

    final CompletableSource source;

    final Action onFinally;

    public CompletableDoFinally(CompletableSource source, Action onFinally) {
        this.source = source;
        this.onFinally = onFinally;
    }

    @Override
    protected void subscribeActual(CompletableObserver s) {
        source.subscribe(new DoFinallyObserver(s, onFinally));
    }

    static final class DoFinallyObserver extends AtomicInteger implements CompletableObserver, Disposable {

        private static final long serialVersionUID = 4109457741734051389L;

        final CompletableObserver actual;

        final Action onFinally;

        Disposable d;

        DoFinallyObserver(CompletableObserver actual, Action onFinally) {
            this.actual = actual;
            this.onFinally = onFinally;
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.d, d)) {
                this.d = d;

                actual.onSubscribe(this);
            }
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
            runFinally();
        }

        @Override
        public void onComplete() {
            actual.onComplete();
            runFinally();
        }

        @Override
        public void dispose() {
            d.dispose();
            runFinally();
        }

        @Override
        public boolean isDisposed() {
            return d.isDisposed();
        }

        void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}
