package me.qgaye;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.function.Function;

public abstract class Pub<T> implements Publisher<T> {

    @Override
    public abstract void subscribe(Subscriber<? super T> subscriber);

    public static <T> Pub<T> just(T... data) {			// just方法进行数据的生产，这里以数组为例
        return new PubArray<>(data);
    }

    public <V> Pub<V> map(Function<? super T, ? extends V> mapper) {   // 这是operator相关函数
        return new PubMap<>(this, mapper);
    }

}
