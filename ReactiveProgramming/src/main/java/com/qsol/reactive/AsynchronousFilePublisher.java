package com.qsol.reactive;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.nio.channels.AsynchronousFileChannel;

public class AsynchronousFilePublisher extends Mono<String> {
    private AsynchronousFileChannel asyncFileChannel;
    /*The CoreSubscriber interface added one Context method. This Context method will return one static object.This means
        all the subscribers will share the same object. I think we are able to set the authentication object inside the
        context object. Interesting.
     */
    public AsynchronousFilePublisher(AsynchronousFileChannel asyncFileChannel) {
        this.asyncFileChannel = asyncFileChannel;
    }
    @Override
    public void subscribe(CoreSubscriber coreSubscriber) {
        coreSubscriber.onSubscribe(new AsynFileReadSubscription(asyncFileChannel, coreSubscriber));
    }
}
