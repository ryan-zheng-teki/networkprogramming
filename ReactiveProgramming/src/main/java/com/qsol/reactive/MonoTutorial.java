package com.qsol.reactive;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoTutorial {
    public static void main(final String[] args) throws InterruptedException {
        final Mono<Integer> intMono = Mono.just(23);

        intMono
                .map(num -> "helloworld")
                .filter(x -> x.startsWith("s"))
                .subscribe(x -> System.out.println(x));


        System.out.println("main thread " + Thread.currentThread().getId());
        Thread.sleep(2000);
    }

    public static void operatorMerging() {
        final Flux just = (Flux) Flux.just(1, 1, 3)
                .subscribe(x -> {
                    x = x + 2;
                    if (x > 1) {
                        System.out.println(x);
                    }
                });
    }

    public static void operatorSplitting() {
        final Flux just = (Flux) Flux.just(1, 1, 3)
                .map(x -> x + 2)
                .filter(x -> x > 1)
                .subscribe(x -> System.out.println(x));
    }
}
