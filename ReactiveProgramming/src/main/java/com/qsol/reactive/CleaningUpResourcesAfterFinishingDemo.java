package com.qsol.reactive;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CleaningUpResourcesAfterFinishingDemo {
    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException, URISyntaxException {
        //traditional opening file and reading
        //traditionalWayOfReading();

        loadFileAsync();
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
     * @param
     * @return
     */
    public static Mono<Integer> readFileFromResource() {
        return getFilePathResource().map(resource -> {
            File file = null;
            String line = null;
            BufferedReader bufferedReader = null;
            try {
                file = resource.getFile();
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                line = bufferedReader.readLine();
            } catch (IOException e) {
                return 12;
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    return 14;
                }
            }
            return Integer.valueOf(line);
        });
    }

    public Mono<Integer> loadTestResource() {
        return Mono.defer(() -> {
            int i = 3;
            if(i % 3 == 0) {
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
        Resource resource = new ClassPathResource("test.txt");
        File file = null;
        String line = null;
        BufferedReader bufferedReader = null;
        try {
            file = resource.getFile();
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            System.out.println(Integer.valueOf(line));
        } catch (IOException e) {
            System.out.println("existing block");
            return ;
        } finally {
            try {
                if(bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.out.println("existing block finally");
            }
        }
    }

    /**
     * Asynio file reading
     */
    public static void loadFileAsync() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        int counter = 0;
        URI uri = new ClassPathResource("test.txt").getURI();
        Path path = Paths.get(uri);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                path, StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Future<Integer> operation = fileChannel.read(buffer, 0);

        while(!operation.isDone()) {
            //busy loop. This will eat CPU
            counter++;
        }

        //when operation finishes, then buffer will contain data.
        System.out.println("counter is "+ counter);
        String fileContent = new String(buffer.array()).trim();
        buffer.clear();
        System.out.println(fileContent);
    }

    /**
     * Asyncio with reactor file reading
     */

}
