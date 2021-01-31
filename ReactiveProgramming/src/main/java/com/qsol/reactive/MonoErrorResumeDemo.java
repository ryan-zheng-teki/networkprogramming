package com.qsol.reactive;

import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class MonoErrorResumeDemo {
    public static void main(final String[] args) {
        fallbackToDefault().subscribe(x -> {
            System.out.println(x);
        });
    }


    /**
     * generating a resource publisher. when error happened, then generate a error publisher.
     */
    public static Mono<Integer> generateResource() {
        final int i = 7;
        if (i % 5 == 0) {
            return Mono.just(5);
        } else {
            return Mono.error(new NullPointerException("holy, there is error when generating the resource"));
        }
    }

    public static Mono<Integer> fallbackToDefault() {
        return generateResource().onErrorResume((e) -> Mono.just(13));
    }

    /**
     * use Mono.usingWhen for the resource
     * read the file, when the content becomes available, then notifiy the subscribers
     * (1)can use the fileChannel.read return value as the resource
     * (2)when the operation.isDone returns
     * (3)I want to start to get the content from the buffer
     * (4)the content of the buffer is sent to subscriber
     * (5)after all, we need to do clean up.
     */
    public static Mono<AsynchronousFileChannel> fileAsyncReadUsingWhen() {
        return Mono.fromCallable(() -> {
            final URI uri = new ClassPathResource("test.txt").getURI();
            final Path path = Paths.get(uri);
            final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                    path, StandardOpenOption.READ);
            return fileChannel;
        });
    }

    /**
     * Mono.usingWhen
     * (1)first parameter The ResourceSupplier. The ResourceSupplier can either
     * be publisher or callable. If its not callable. Then it must be publisher.
     * <p>
     * (2)second parameter: what to do to this resource. And will return a new
     * SourcePublisher. This is the real data publisher that will be subscribed
     * by the users
     * <p>
     * (3) the asyncCleanup. After the resource is being used, what to do with this
     * resource. Either close it or clean it.
     */
    public static void subscribeToBufferedDataAndReturnNewResource() {
        final Mono<String> stringResource = Mono.usingWhen(
                fileAsyncReadUsingWhen(),
                (AsynchronousFileChannel fileChannel) -> {
                    final ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //the second parameter says where to put the content into the buffer
                    final Future<Integer> operation = fileChannel.read(buffer, 0);
                    return Mono.just(new String(buffer.array()).trim());
                },
                (AsynchronousFileChannel fileChannel) -> {
                    try {
                        fileChannel.close();
                    } catch (final IOException e) {
                        return Mono.empty();
                    }
                    return Mono.empty();
                });
    }
}
