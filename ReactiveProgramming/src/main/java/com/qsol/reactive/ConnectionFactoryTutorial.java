package com.qsol.reactive;

public class ConnectionFactoryTutorial {
    public static void main(String[] args) {
        new ConnectionFactory().getConnection().subscribe(connection -> {
            System.out.println(connection.getName());
        });
    }
}
