package me.qgaye;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class PubArray<T> extends Pub<T> {

    private T[] array;

    @SafeVarargs
    public PubArray(T... data) {
        this.array = data;
    }

    @Override
    public void subscribe(Subscriber<? super T> actual) {
        actual.onSubscribe(new ArraySubscription<>(actual, array));
    }

    static class ArraySubscription<T> implements Subscription {

        final Subscriber<? super T> actual;
        final T[] array;
        int index;
        volatile boolean cancelled;

        public ArraySubscription(Subscriber<? super T> actual, T[] array) {
            this.actual = actual;
            this.array = array;
            this.index = 0;
            this.cancelled = false;
        }

        @Override
        public void request(long n) {
            if (cancelled) {
                return;
            }
            long length = array.length;
            int i = index;
            int e = 0;
            for (; e <= n && i < length; e++) {
                if (cancelled) {
                    return;
                }
                if (++i == length) {
                    cancelled = true;
                }
                actual.onNext(array[index++]);  // 回调订阅者的onNext方法传递元素
            }
            if (index == length) {
                actual.onComplete();    // 回调订阅者的onComplete方法；
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
        }   // 内部类Subscription
    }

}
