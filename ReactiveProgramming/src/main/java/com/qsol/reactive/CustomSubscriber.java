package com.qsol.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CorePublisher;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

public class CustomSubscriber implements Subscriber<Integer>, Subscription{
    private Integer value;

    private Subscriber<Integer> downStreamSubscriber;

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Integer integer) {
        this.value = integer;
        Mono publisher = Mono.just(20);
    }

    @Override
    public void onError(Throwable throwable) {
        return ;
    }

    @Override
    public void onComplete() {
        return ;
    }

    @Override
    public void request(long l) {
    }

    @Override
    public void cancel() {

    }
}
