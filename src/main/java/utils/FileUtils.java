package utils;

import com.google.common.base.Charsets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileUtils {
    public static void makeParentDirsIfNecessary(File file) throws IOException {
        File dir = file.getAbsoluteFile().getParentFile();
        if (dir.exists()) {
            return;
        }
        if (!dir.mkdirs()) {
            throw new IOException(String.format("Fail to create directory [%s]", dir));
        }
    }

    public static List<String> readLines(File dbFile) throws IOException {
        return Files.readAllLines(dbFile.toPath(), Charsets.UTF_8);
    }

    public static void writeLines(File file, List<String> lines) throws IOException {
        try (FileOutputStream fout = new FileOutputStream(file)) {
            for (String line : lines) {
                fout.write((line + "\n").getBytes());
            }
        }
    }
}
