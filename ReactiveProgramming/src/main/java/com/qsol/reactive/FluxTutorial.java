package com.qsol.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static void methodWithException()  throws ExecutionException, InterruptedException {
        final ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        int seconds = LocalTime.now().getSecond();
        List<Integer> source;
        if (seconds % 2 == 0) {
            source = IntStream.range(1, 11).boxed().collect(Collectors.toList());
        }
        else if (seconds % 3 == 0) {
            source = IntStream.range(0, 4).boxed().collect(Collectors.toList());
        }
        else {
            source = Arrays.asList(1, 2, 3, 4);
        }

        executor.submit(() -> source.get(5))  //line 76
                .get();
    }
}
