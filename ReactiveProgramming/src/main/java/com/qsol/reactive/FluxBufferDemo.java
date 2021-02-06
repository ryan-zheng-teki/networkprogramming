package com.qsol.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxBufferDemo {
    public static void main(final String[] args) {
        Flux.range(1, 20)
                .delayElements(Duration.ofMillis(500))
                .buffer(5)  // collect the items in batches of 5
                .subscribe(l -> System.out.println("Received :: " + l));
    }
}
