package pink.zak.minestom.operadora.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.world.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.utils.data.FileUtils;

import java.nio.file.Path;

public class OperadoraConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperadoraConfig.class);

    private final String ip;
    private final int port;
    private final boolean onlineMode;
    private final int chunkViewDistance;
    private final int entityViewDistance;
    private final int compressionThreshold;

    private final String brandName; // What is displayed in the F3 menu
    private final Difficulty difficulty;

    private final ProxyType proxyType;
    private final String velocitySecret;

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

        this.proxyType = config.getEnum(ProxyType.class, "proxy.type");
        this.velocitySecret = System.getProperty("minestom.operadora.velocity-secret", config.getString("proxy.velocity-secret"));
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

        this.setupProxy();
        MinecraftServer.setCompressionThreshold(this.compressionThreshold);
        MinecraftServer.setBrandName(this.brandName);
        MinecraftServer.setDifficulty(this.difficulty);
    }

    private enum ProxyType {
        NONE,
        BUNGEE_CORD,
        VELOCITY
    }

    private void setupProxy() {
        switch (this.proxyType) {
            case VELOCITY -> {
                if (this.velocitySecret.isEmpty()) {
                    LOGGER.error("Velocity support is enabled but the velocity secret is empty");
                    System.exit(1000); // Might as well terminate the server, it won't act as expected otherwise
                } else {
                    VelocityProxy.enable(this.velocitySecret);
                    LOGGER.info("Velocity support has been enabled");
                }
            }
            case BUNGEE_CORD -> {
                BungeeCordProxy.enable();
                LOGGER.info("BungeeCord support has been enabled");
            }
            case NONE -> {}
        }
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
