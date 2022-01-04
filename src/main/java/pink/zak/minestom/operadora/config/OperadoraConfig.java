package pink.zak.minestom.operadora.config;

import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.world.Difficulty;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.utils.data.FileConverterUtils;
import pink.zak.minestom.operadora.utils.data.FileUtils;

import java.nio.file.Path;

public class OperadoraConfig {
    private final String ip;
    private final int port;
    private final boolean onlineMode;
    private final int chunkViewDistance;
    private final int entityViewDistance;
    private final int compressionThreshold;

    private final String brandName; // What is displayed in the F3 menu
    private final Difficulty difficulty;

    public OperadoraConfig() {
        Path filePath = Operadora.getBasePath().resolve("server-properties.conf");
        FileUtils.saveResourceIfNotExists(filePath, "server-properties.conf");

        Config config = ConfigFactory.parseFile(filePath.toFile());

        this.ip = config.getString("ip");
        this.port = config.getInt("port");
        this.onlineMode = config.getBoolean("online-mode");
        this.chunkViewDistance = config.getInt("chunk-view-distance");
        this.entityViewDistance = config.getInt("entity-view-distance");
        this.compressionThreshold = config.getInt("compression-threshold");

        this.brandName = config.getString("brand-name");
        this.difficulty = config.getEnum(Difficulty.class, "difficulty");
    }

    public void applySystemProperties() {
        if (System.getProperty("minestom.chunk-view-distance") == null)
            System.setProperty("minestom.chunk-view-distance", String.valueOf(this.chunkViewDistance));

        if (System.getProperty("minestom.entity-view-distance") == null)
            System.setProperty("minestom.entity-view-distance", String.valueOf(this.entityViewDistance));
    }

    public void applyServerSettings() {
        if (this.onlineMode)
            MojangAuth.init();

        MinecraftServer.setCompressionThreshold(this.compressionThreshold);
        MinecraftServer.setBrandName(this.brandName);
        MinecraftServer.setDifficulty(this.difficulty);
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public int getChunkViewDistance() {
        return this.chunkViewDistance;
    }

    public int getEntityViewDistance() {
        return this.entityViewDistance;
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
}
