package pink.zak.minestom.operadora.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.update.OperadoraVersion;
import pink.zak.minestom.operadora.utils.data.FileUtils;

public record OperadoraMeta(OperadoraVersion version) {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperadoraMeta.class);

    public static OperadoraMeta load() {
        try {
            String jsonString = new String(FileUtils.class.getClassLoader().getResourceAsStream("operadora.json").readAllBytes());
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            return new OperadoraMeta(
                    new OperadoraVersion(
                            jsonObject.get("version").getAsString(),
                            jsonObject.get("commitHash").getAsString(),
                            jsonObject.get("buildNumber").getAsInt()
                    )
            );
        } catch (Exception ex) {
            LOGGER.error("Error loading OperadoraMeta", ex);
            return null;
        }
    }
}
