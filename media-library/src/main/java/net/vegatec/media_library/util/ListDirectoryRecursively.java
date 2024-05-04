package net.vegatec.media_library.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class ListDirectoryRecursively implements Callable<Void> {

    private final Path directory;

    public ListDirectoryRecursively(Path directory) {
        this.directory = directory;
    }

    @Override
    public Void call() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    new ListDirectoryRecursively(path).call();
                } else {
                    System.out.println(path.toString());
                }
            }
        }
        return null;
    }
}
