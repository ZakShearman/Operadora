package pink.zak.minestom.operadora.utils.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BasicJsonRepository {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BasicJsonRepository.class);
    protected static final Gson GSON = new GsonBuilder().create();
    protected final Path path;

    public BasicJsonRepository(Path path) {
        this.path = path;
    }

    protected void save(JsonElement jsonElement) {
        if (!Files.exists(this.path)) {
            File file = this.path.toFile();
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                LOGGER.error("", ex);
            }
        }

        try (Writer writer = Files.newBufferedWriter(this.path)) {
            GSON.toJson(jsonElement, writer);
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }
    }
}
