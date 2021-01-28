package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class MonoErrorResumeDemo {
    public static void main(String[] args) {
        fallbackToDefault().subscribe(x -> {
            System.out.println(x);
        });
    }


    /**
     * generating a resource publisher. when error happened, then generate a error publisher.
     */
    public static Mono<Integer> generateResource() {
        int i = 7;
        if( i % 5 == 0) {
            return Mono.just(5);
        } else {
            return Mono.error(new NullPointerException("holy, there is error when generating the resource"));
        }
    }

    public static Mono<Integer> fallbackToDefault() {
        return generateResource().onErrorResume((e) -> Mono.just(13));
    }
}
