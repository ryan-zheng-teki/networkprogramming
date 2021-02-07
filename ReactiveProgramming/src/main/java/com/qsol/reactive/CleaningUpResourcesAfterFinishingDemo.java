package com.qsol.reactive;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CleaningUpResourcesAfterFinishingDemo {
    public static void main(final String[] args) throws InterruptedException, IOException, ExecutionException, URISyntaxException {
        //traditional opening file and reading
        //traditionalWayOfReading();
        //readFileFromResource();
        //loadFileAsync();
        //monoUsingDemo();
        Hooks.onOperatorDebug();
        useFileOutputResource();
    }


    public static Mono<Resource> getFilePathResource() {
        return Mono.just(new ClassPathResource("test.txt"));
    }

    /**
     * What i want to apply to the resource is to get the file, and open the
     * file. If the file open failed, i wanna close the resource.
     * If the file open is okay, i wanna read the content from the file. and
     * return the new resource. The real Subscriber is subscribing to the data read
     * from the file.
     * <p>
     * .onErrorMap(RuntimeException.class, e -> {
     * return e.getCause();
     * });
     *
     * @param
     * @return
     */
    public static void readFileFromResource() {
        final Mono<Integer> dataResource = getFilePathResource().map(resource -> {
            File file = null;
            String line = null;
            BufferedReader bufferedReader = null;
            try {
                //asynchronous
                file = resource.getFile();
                final FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                line = bufferedReader.readLine();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
            return Integer.valueOf(line);
        }).onErrorMap(RuntimeException.class, e -> {
            return e.getCause();
        });

        dataResource.subscribe(
                (x) -> {
                    System.out.println("onNext " + x);
                },
                (e) -> {
                    System.out.println("OnError " + e.getMessage());
                },
                (() -> System.out.println("OnComplete")));
    }

    public Mono<Integer> loadTestResource() {
        return Mono.defer(() -> {
            final int i = 3;
            if (i % 3 == 0) {
                return Mono.just(i);
            } else {
                return Mono.error(new NullPointerException("invalid argument"));
            }
        });
    }

    /**
     * Traditional way of reading the file
     * 1. open the file
     * 2. read content from the file
     */

    public static void traditionalWayOfReading() {
        final Resource resource = new ClassPathResource("test.txt.bak");
        File file = null;
        String line = null;
        BufferedReader bufferedReader = null;
        try {
            file = resource.getFile();
            final FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            System.out.println(Integer.valueOf(line));
        } catch (final IOException e) {
            System.out.println(e.getMessage());
            return;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (final IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Asynio file reading. We can provide a CompletionHandler to the file read.
     * When the FileRead operation is completed, then the CompletionHandler will be
     * invoked. The current thread can actually process other task. The completionHandler
     * is called asynchronously.
     */
    public static void loadFileAsync() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        final int counter = 0;
        final URI uri = new ClassPathResource("test.txt.bak").getURI();
        final Path path = Paths.get(uri);
        final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                path, StandardOpenOption.READ);
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        /*
         The file read completionHandler is actually invoked in a different thread.
         maybe AsynchronousFileChannel channel already starts a different thread.This
         explains why fileChannel.get() will block the current thread. Because it is
         actually waiting for another thread to finish.
        */
        fileChannel.read(buffer, 0, buffer,
                new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(final Integer result, final ByteBuffer attachment) {
                        System.out.println("result = " + result);
                        System.out.println("thread id is " + Thread.currentThread().getId());
                        attachment.flip();
                        final byte[] data = new byte[attachment.limit()];
                        attachment.get(data);
                        System.out.println(new String(data));
                        attachment.clear();
                    }

                    @Override
                    public void failed(final Throwable exc, final ByteBuffer attachment) {

                    }
                });
        System.out.println("counter is " + counter);
        System.out.println("thread id is " + Thread.currentThread().getId());
        Thread.sleep(10000);
        //when operation finishes, then buffer will contain data.
        final String fileContent = new String(buffer.array()).trim();
        buffer.clear();
        System.out.println(fileContent);
    }

    /**
     * fromCallable takes one Callable interface which might throw exception.
     */
    public static Mono<AsynchronousFileChannel> asyncFileChannelResource() {
        return Mono.fromCallable(() -> {
            final URI uri = new ClassPathResource("test.txt").getURI();
            final Path path = Paths.get(uri);
            final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                    path, StandardOpenOption.READ);
            return fileChannel;
        }).onErrorResume(e -> {
            try {
                final URI uri = new ClassPathResource("test.txt.bak").getURI();
                final Path path = Paths.get(uri);
                final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                        path, StandardOpenOption.READ);
                return Mono.just(fileChannel);
            } catch (final IOException exception) {
                return Mono.error(e);
            }
        });
    }

    /**
     * Mono.usingWhen
     * (1)first parameter The ResourceSupplier. The ResourceSupplier can either
     * be publisher or callable. If its not callable. Then it must be publisher.
     * <p>
     * (2)second parameter: what to do to this resource. And will return a new
     * SourcePublisher. This is the real data publisher that will be subscribed
     * by the users
     * <p>
     * (3) the asyncCleanup. After the resource is being used, what to do with this
     * resource. Either close it or clean it.
     * (4) I have to use BlockingGet to get the value.
     */
    public static Mono<String> fileContentAsResource() {
        System.out.println("main thread is " + Thread.currentThread().getId());
        final Mono<String> fileContentResource = Mono.usingWhen(
                asyncFileChannelResource(),
                (AsynchronousFileChannel fileChannel) -> {
                    return new AsyncFileReadPublisher(fileChannel);
                },
                (AsynchronousFileChannel fileChannel) -> {
                    try {
                        fileChannel.close();
                    } catch (final IOException e) {
                        return Mono.empty();
                    }
                    return Mono.empty();
                });
        return fileContentResource;
    }

    public static void useFileOutputResource() throws InterruptedException {
        Mono.just("332").publishOn(Schedulers.boundedElastic())
                .map(x -> {
                    final String output = x + fileContentAsResource().block();
                    return output;
                })
                .subscribe(x -> System.out.println(x));
        System.out.println("Hey, i am here");
        Thread.sleep(2000);
    }

    public static void monoUsingDemo() {
        final List<Integer> intArrays = new ArrayList<>() {
            {
                this.add(12);
                this.add(30);
            }
        };
        System.out.println("init length " + intArrays.size());
        Flux.using(() -> intArrays,
                (list) -> Flux.fromIterable(list),
                (list) -> list.clear()).subscribe(x -> System.out.println(x));
        System.out.println("after cleaning" + intArrays.size());
    }
}
