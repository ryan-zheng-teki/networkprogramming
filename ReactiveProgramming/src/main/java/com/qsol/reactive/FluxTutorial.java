package com.qsol.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FluxTutorial {
    public static void main(String[] args) throws InterruptedException {
        //From user perspective, this api is very simple.
        coldFluxExample2();
        //testSubscribeToMonoFunction();
    }

    public static void coldFluxExample1() {
        Flux.range(5,3)
                .map(i -> i + 3)
                .filter( i -> i % 2 == 0 )
                .buffer(3)
                .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.parallel())
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


    public static void testSubscribeToMonoFunction() {
        Mono.just(simulateDataFromNetwork()).flatMapMany(Flux::fromIterable).subscribe( element -> {
            System.out.println("going to print "+element);
        });
    }


    private static Collection<String> simulateDataFromNetwork() {
        System.out.println("I am called after subscribe");
        return new ArrayList<String>() {
            {
                add("test-one");
                add("test-two");
            }
        };
    }

    /*
    The map actually throws one exception. The map is able to take one lamda
    for converting the exception from checked exception to unchecked exception.
     */
    private static void startingAnotherPipelineInsideOnNext() {
        Flux.range(0, Runtime.getRuntime().availableProcessors() * 2)
                .subscribeOn(Schedulers.parallel())
                .map(i -> {
                    CountDownLatch latch = new CountDownLatch(1);

                    Mono.delay(Duration.ofMillis(i * 100))
                            .subscribe(it -> latch.countDown());
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return i;
                })
                .blockLast();
    }
}
