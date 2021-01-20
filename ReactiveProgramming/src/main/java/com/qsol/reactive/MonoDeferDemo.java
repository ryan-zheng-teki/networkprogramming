package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class MonoDeferDemo {
    public static void main(String[] args) {
        Mono.defer(() ->
             Mono.just(3)).subscribe(x -> System.out.println(x));
    }
}
