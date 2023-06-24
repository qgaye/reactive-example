package me.qgaye;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Sub<T> implements Subscriber<T> {

    private Subscription subscription;
    private int count;

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("Start");
        this.subscription = subscription;
        this.subscription.request(2);
    }

    @Override
    public void onNext(T t) {
        System.out.println("get data: " + t);
        count += 1;
        if (count >= 2) {
            count = 0;
            subscription.request(2);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        System.out.println("all done");
    }
}