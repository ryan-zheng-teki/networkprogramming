package com.qsol.reactive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ReactiveTutorial {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        combineAsynTogether();
        // exceptionallyThrow();
        // timeoutTrial();
        // combineApplySequentialTask();
    }


    public static String getCurrentThreadId() throws InterruptedException {
        System.out.println("current thread is "+Thread.currentThread().getId());
        Thread.sleep(3000);
        return String.valueOf(Thread.currentThread().getId());
    }


    public static void combineAsynTogether() throws ExecutionException, InterruptedException {
        Future future = CompletableFuture.supplyAsync(() -> {
            try {
                return getCurrentThreadId();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            try {
                return getCurrentThreadId();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }), (l, r) -> l + r)
                .thenAcceptAsync(x ->
                {
                    System.out.println("current thread is "+Thread.currentThread().getId());
                });
        System.out.println(future.get());
    }

    /*public static void exceptionallyThrow() {
        CompletableFuture.failedFuture(new Throwable("hallo"))
                .thenCombineAsync(CompletableFuture.supplyAsync(() -> getCurrentThreadId()), (o, s) ->  o + s)
                .exceptionally(x -> x.getMessage());
    }
    */

    /*
    public static void timeoutTrial() throws InterruptedException, ExecutionException {
        CompletableFuture cf = new CompletableFuture();
        cf.orTimeout(5, TimeUnit.SECONDS);

        Thread.sleep(3000);
        cf.get();
    }
    */

    public static void combineApplySequentialTask() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getCurrentThreadId();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }).thenApply(x ->{
            String y = x + 20;
            System.out.println(y);
            return y;
        });
    }
}
