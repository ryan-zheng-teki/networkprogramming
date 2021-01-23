package com.qsol.reactive;

import reactor.core.publisher.Flux;

public class ErrorHandlingDemo {
    public static void main(String[] args) {
        Flux<Integer> fluxFromJust = Flux.just(1, 2,3,4,5)
                .concatWith(Flux.error(new RuntimeException("Test")))
                .concatWith(Flux.just(6));
        fluxFromJust.subscribe(
                (it)-> System.out.println("Number is " + it),  // OnNext
                (e) -> e.printStackTrace(),                    //OnError
                () -> System.out.println("subscriber Completed") //onComplete
        );
    }
}
