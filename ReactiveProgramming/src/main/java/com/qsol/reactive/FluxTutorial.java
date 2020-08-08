package com.qsol.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxTutorial {
    public static void main(String[] args) throws InterruptedException {
        //From user perspective, this api is very simple.
        coldFluxExample2();
    }

    public static void coldFluxExample1() {
        Flux.range(5,3)
                .map(i -> i + 3)
                .filter( i -> i % 2 == 0 )
                .buffer(3)
                .subscribe(x -> System.out.println(x));
    }
    public static void coldFluxExample2() throws InterruptedException {
        Flux<Long> clockTicks = Flux.interval(Duration.ofSeconds(1));

        clockTicks.subscribe(tick -> System.out.println("clock1 " + tick + "s"));
        clockTicks.subscribe(tick -> System.out.println("\tclock2 " + tick + "s"));

        Thread.sleep(20000);
    }
}
