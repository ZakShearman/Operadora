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

public record OperadoraConfig(String ip, int port, boolean onlineMode,
                              int chunkViewDistance, int entityViewDistance, int compressionThreshold,
                              String brandName /* What is displayed in the F3 menu */, Difficulty difficulty,
                              ProxyType proxyType, String velocitySecret) {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperadoraConfig.class);

    public static OperadoraConfig load() {
        Path filePath = Operadora.getBasePath().resolve("server-properties.conf");
        FileUtils.saveResourceIfNotExists(filePath, "server-properties.conf");

        Config config = ConfigFactory.parseFile(filePath.toFile());

        String ip = config.getString("ip");
        int port = config.getInt("port");
        boolean onlineMode = config.getBoolean("online-mode");
        int chunkViewDistance = config.getInt("chunk-view-distance");
        int entityViewDistance = config.getInt("entity-view-distance");
        int compressionThreshold = config.getInt("compression-threshold");

        String brandName = config.getString("brand-name");
        Difficulty difficulty = config.getEnum(Difficulty.class, "difficulty");

        ProxyType proxyType = config.getEnum(ProxyType.class, "proxy.type");
        String velocitySecret = System.getProperty("minestom.operadora.velocity-secret", config.getString("proxy.velocity-secret"));

        return new OperadoraConfig(ip, port, onlineMode, chunkViewDistance, entityViewDistance, compressionThreshold, brandName, difficulty, proxyType, velocitySecret);
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
            case NONE -> {
            }
        }
    }

    private enum ProxyType {
        NONE,
        BUNGEE_CORD,
        VELOCITY
    }
}
