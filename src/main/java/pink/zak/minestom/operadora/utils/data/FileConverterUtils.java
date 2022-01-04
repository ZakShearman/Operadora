package pink.zak.minestom.operadora.utils.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileConverterUtils {

    public static @Nullable JsonElement getAsJson(File file) {
        try {
            return JsonParser.parseReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
