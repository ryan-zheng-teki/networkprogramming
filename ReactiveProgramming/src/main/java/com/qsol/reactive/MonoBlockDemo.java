package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class MonoBlockDemo {
    public static void main(final String[] args) {
        System.out.println(Mono.just(12).block());

        Mono.just(5).map(x -> {
            /*
            what i wanna do here is read a file based on the input
            and i want to perform a multiply function of the input+x
            and use the result to signal the down stream subscribers
             */
            
            return x;
        });
    }
}
