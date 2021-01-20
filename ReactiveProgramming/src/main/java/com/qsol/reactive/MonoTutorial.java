package com.qsol.reactive;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MonoTutorial {
    public static void main(String[] args) throws InterruptedException {
        Mono<Integer> intMono = Mono.just(23);

        Mono<String> stringMono = intMono.map(num -> "helloworld");


        System.out.println("main thread " + Thread.currentThread().getId());
        Thread.sleep(2000);


    }
}
