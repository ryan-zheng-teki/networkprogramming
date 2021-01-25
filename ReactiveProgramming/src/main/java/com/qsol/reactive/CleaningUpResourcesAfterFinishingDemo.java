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
import java.nio.file.Path;
import java.nio.file.Paths;

public class CleaningUpResourcesAfterFinishingDemo {
    public static void main(String[] args) {
        //traditional opening file and reading
        Path path = Paths.get("F:\\netjs\\test.txt");
    }

    public Mono<Resource> getFilePathResource() {
        return Mono.just(new ClassPathResource("test.txt"));
    }

    /**
     * What i want to apply to the resource is to get the file, and open the
     * file. If the file open failed, i wanna close the resource.
     * If the file open is okay, i wanna read the content from the file. and
     * return the new resource. The real Subscriber is subscribing to the data read
     * from the file.
     * @param resource
     * @return
     */
    public Mono<Integer> readFileFromResource() throws IOException {
        getFilePathResource().map(resource -> {
            File file = resource.getFile();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            return Mono.just(Integer.valueOf(line));
        })

    }

    public Mono<File> loadTestResource() {
        return Mono.defer(() -> {

        });
    }
}
