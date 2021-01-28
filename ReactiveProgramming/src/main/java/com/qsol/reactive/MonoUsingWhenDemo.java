package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class MonoUsingWhenDemo {
    public static void main(String[] args) {
        Mono.usingWhen(Mono.just(10),
                (x)-> {
                        x = x +10;
                        System.out.println("inside function "+x);
                        return Mono.just(x);
                    },
                (x)-> {
                    x = x + 40;
                    System.out.println("inside complete"+x);
                    return Mono.just(x);
                })
                .subscribe(x -> System.out.println(x));
    }



}
