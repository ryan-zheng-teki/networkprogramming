package com.qsol.reactive;


import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class MonoTutorial {
    public static void main(String[] args) {
        List<Integer> elements = new ArrayList<>();
        Flux just = (Flux) Flux.just("1", "2", "3")
                .map(x -> x + 2)
                .subscribe(x -> System.out.println(x));


        System.out.println("hello world");

    }
}
