package pink.zak.minestom.operadora.utils.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    public static void saveResourceIfNotExists(Path path, String resource) {
        if (!Files.exists(path)) {
            try {
                Files.write(path, FileUtils.class.getClassLoader().getResourceAsStream(resource).readAllBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
