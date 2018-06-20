/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ollyice.library.rxlifecycle;

import cn.ollyice.library.reactivestreams.Publisher;
import cn.ollyice.library.rxjava.BackpressureStrategy;
import cn.ollyice.library.rxjava.Completable;
import cn.ollyice.library.rxjava.CompletableSource;
import cn.ollyice.library.rxjava.CompletableTransformer;
import cn.ollyice.library.rxjava.Flowable;
import cn.ollyice.library.rxjava.FlowableTransformer;
import cn.ollyice.library.rxjava.Maybe;
import cn.ollyice.library.rxjava.MaybeSource;
import cn.ollyice.library.rxjava.MaybeTransformer;
import cn.ollyice.library.rxjava.Observable;
import cn.ollyice.library.rxjava.ObservableSource;
import cn.ollyice.library.rxjava.ObservableTransformer;
import cn.ollyice.library.rxjava.Single;
import cn.ollyice.library.rxjava.SingleSource;
import cn.ollyice.library.rxjava.SingleTransformer;

import javax.annotation.ParametersAreNonnullByDefault;

import static cn.ollyice.library.rxlifecycle.internal.Preconditions.checkNotNull;

/**
 * Transformer that continues a subscription until a second Observable emits an event.
 */
@ParametersAreNonnullByDefault
public final class LifecycleTransformer<T> implements ObservableTransformer<T, T>,
                                                      FlowableTransformer<T, T>,
                                                      SingleTransformer<T, T>,
                                                      MaybeTransformer<T, T>,
                                                      CompletableTransformer
{
    final Observable<?> observable;

    LifecycleTransformer(Observable<?> observable) {
        checkNotNull(observable, "observable == null");
        this.observable = observable;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil(observable);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST));
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil(observable.firstOrError());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil(observable.firstElement());
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return Completable.ambArray(upstream, observable.flatMapCompletable(Functions.CANCEL_COMPLETABLE));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        LifecycleTransformer<?> that = (LifecycleTransformer<?>) o;

        return observable.equals(that.observable);
    }

    @Override
    public int hashCode() {
        return observable.hashCode();
    }

    @Override
    public String toString() {
        return "LifecycleTransformer{" +
            "observable=" + observable +
            '}';
    }
}
