package com.qsol.reactive;

import reactor.core.publisher.Mono;

public class ConnectionFactory {
    public ConnectionFactory() {

    }
    public Mono<CustomConnection> getConnection() {
        return Mono.defer(() -> {
           return Mono.just(new CustomConnection("newConnection"));
        });
    }
}
