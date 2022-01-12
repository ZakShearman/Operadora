package pink.zak.minestom.operadora.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.utils.data.FileUtils;

public record OperadoraMeta(String version, String buildNumber, String commitHash) {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperadoraMeta.class);

    public static OperadoraMeta load() {
        try {
            String jsonString = new String(FileUtils.class.getClassLoader().getResourceAsStream("operadora.json").readAllBytes());
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            System.out.println(jsonString);
            System.out.println(jsonObject);
            return new OperadoraMeta(
                jsonObject.get("version").getAsString(),
                jsonObject.get("buildNumber").getAsString(),
                jsonObject.get("commitHash").getAsString()
            );
        } catch (Exception ex) {
            LOGGER.error("Error loading OperadoraMeta (MANIFEST.MF)", ex);
            return null;
        }
    }

    public String shortCommitHash() {
        return this.commitHash.substring(0, 7);
    }
}
