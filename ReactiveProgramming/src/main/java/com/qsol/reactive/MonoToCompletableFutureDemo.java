package com.qsol.reactive;

import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MonoToCompletableFutureDemo {
    public static void main(final String[] args) throws ExecutionException, InterruptedException {
        Mono.just(4).toFuture();
        Integer[] integers = {1, 3, 5};
        Flux.fromArray(integers).collectList().toFuture();
    }

    public static Mono<AsynchronousFileChannel> asyncFileChannelResource() {
        return Mono.fromCallable(() -> {
            final URI uri = new ClassPathResource("test.txt").getURI();
            final Path path = Paths.get(uri);
            final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                    path, StandardOpenOption.READ);
            return fileChannel;
        }).onErrorResume(e -> {
            try {
                final URI uri = new ClassPathResource("test.txt.bak").getURI();
                final Path path = Paths.get(uri);
                final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                        path, StandardOpenOption.READ);
                return Mono.just(fileChannel);
            } catch (final IOException exception) {
                return Mono.error(e);
            }
        });
    }
}
