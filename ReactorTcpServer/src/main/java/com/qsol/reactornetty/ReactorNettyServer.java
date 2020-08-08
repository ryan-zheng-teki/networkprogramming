package com.qsol.reactornetty;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

public class ReactorNettyServer {
    public static void main(String[] args) {
        DisposableServer server =
                TcpServer.create()
                        .handle((inbound, outbound) -> outbound.sendString(Mono.just("hello")))
                        .host("localhost")
                        .port(8080)
                        .bindNow();

        server.onDispose()
                .block();
    }
}
