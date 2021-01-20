package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class MonoBlockDemo {
    public static void main(String[] args) {
        System.out.println(Mono.just(12).block());
    }
}
