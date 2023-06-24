package me.qgaye;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

public class PubMap<T, R> extends Pub<R> {

    private final Pub<? extends T> source;
    private final Function<? super T, ? extends R> mapper;

    public PubMap(Pub<? extends T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> actual) {
        source.subscribe(new MapSubscriber<>(actual, mapper));
    }

    static final class MapSubscriber<T, R> implements Subscriber<T>, Subscription {
        private final Subscriber<? super R> actual;
        private final Function<? super T, ? extends R> mapper;
        boolean done;
        Subscription subscriptionOfUpstream;

        public MapSubscriber(Subscriber<? super R> actual, Function<? super T, ? extends R> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscriptionOfUpstream = subscription;
            actual.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            try {
                R v = mapper.apply(t);
                actual.onNext(v);
            } catch (Throwable throwable) {
                onError(throwable);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (done) {
                return;
            }
            done = true;
            actual.onError(throwable);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            actual.onComplete();
        }

        @Override
        public void request(long n) {
            this.subscriptionOfUpstream.request(n);
        }

        @Override
        public void cancel() {
            this.subscriptionOfUpstream.cancel();
        }

    }

}
