package com.qsol.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class ReactorDebuggingDemo {
    public static void main(final String[] args) {
        Flux.range(0, Runtime.getRuntime().availableProcessors() * 2)
                .subscribeOn(Schedulers.parallel())
                .map(i -> {
                    final CountDownLatch latch = new CountDownLatch(1);
                    System.out.println("I am here");

                    Mono.delay(Duration.ofMillis(i * 100))
                            .subscribe(it -> latch.countDown());

                    try {
                        latch.await();
                    } catch (final InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return i;
                })
                .blockLast();

        System.out.println("I reached here");
    }
}
