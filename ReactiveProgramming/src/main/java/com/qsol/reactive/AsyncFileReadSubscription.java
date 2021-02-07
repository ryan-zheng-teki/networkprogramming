package com.qsol.reactive;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;


public class AsyncFileReadSubscription implements Subscription {
    private final AsynchronousFileChannel asyncFileChannel;
    private final CoreSubscriber coreSubscriber;
    ByteBuffer buffer;

    public AsyncFileReadSubscription(final AsynchronousFileChannel asyncFileChannel, final CoreSubscriber coreSubscriber) {
        this.asyncFileChannel = asyncFileChannel;
        this.coreSubscriber = coreSubscriber;
        this.buffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void request(final long l) {
        this.asyncFileChannel.read(this.buffer, 0, this.coreSubscriber,
                new CompletionHandler<Integer, CoreSubscriber>() {
                    @Override
                    public void completed(final Integer result, final CoreSubscriber subscriber) {
                        System.out.println("thread id is " + Thread.currentThread().getId());
                        //this will be in a different thread.
                        AsyncFileReadSubscription.this.buffer.flip();
                        final byte[] data = new byte[AsyncFileReadSubscription.this.buffer.limit()];
                        AsyncFileReadSubscription.this.buffer.get(data);
                        AsyncFileReadSubscription.this.buffer.clear();
                        subscriber.onNext(new String(data));
                        //don't forget to call onComplete for the subscriber
                        subscriber.onComplete();
                    }

                    @Override
                    public void failed(final Throwable exc, final CoreSubscriber attachment) {
                    }
                });
    }

    @Override
    public void cancel() {

    }
}
