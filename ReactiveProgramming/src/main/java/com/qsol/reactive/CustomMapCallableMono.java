package com.qsol.reactive;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.Callable;

public class CustomMapCallableMono extends Mono {
    public static void main(final String[] args) {
        CustomMapCallableMono.map(() -> {
            throw new IOException();
        });
    }


    @Override
    public void subscribe(final CoreSubscriber coreSubscriber) {

    }

    public static final <R> Mono<R> map(final Callable<R> callable) {
        return Mono.fromCallable(callable);
    }
}
