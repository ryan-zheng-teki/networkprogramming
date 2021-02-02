package com.qsol.reactive;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

/**
 * How to clean up this subscription?
 */
public class AsynFileReadSubscription implements Subscription {
    private AsynchronousFileChannel asyncFileChannel;
    private CoreSubscriber coreSubscriber;
    ByteBuffer buffer;



    public AsynFileReadSubscription(AsynchronousFileChannel asyncFileChannel, CoreSubscriber coreSubscriber) {
        this.asyncFileChannel = asyncFileChannel;
        this.coreSubscriber = coreSubscriber;
        this.buffer = ByteBuffer.allocate(1024);
    }
    @Override
    public void request(final long l) {
        asyncFileChannel.read(buffer, 0,  coreSubscriber,
                new CompletionHandler<Integer, CoreSubscriber>() {

            //the result parameter is the number of bytes already read.
                    @Override
                    public void completed(final Integer result, final CoreSubscriber subscriber) {
                        System.out.println("result = " + result);
                        System.out.println("thread id is "+ Thread.currentThread().getId());
                        //this will be in a different thread.
                        buffer.flip();
                        final byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        buffer.clear();
                        subscriber.onNext(result);
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
