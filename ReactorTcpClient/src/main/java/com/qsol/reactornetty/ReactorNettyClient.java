package com.qsol.reactornetty;

import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

public class ReactorNettyClient {
    public static void main(String[] args) {
        Connection connection =
                TcpClient.create()
                        .host("localhost")
                        .port(8080)
                        .connectNow();

        connection.onDispose()
                .block();
    }
}
