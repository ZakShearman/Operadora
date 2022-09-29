package pink.zak.minestom.operadora.module.spawn;

import com.typesafe.config.Config;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;

import java.util.UUID;
import java.util.regex.Pattern;

public class SpawnModule extends Module {
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private static final Logger LOGGER = LoggerFactory.getLogger(SpawnModule.class);

    private Pos spawn;
    private UUID instanceId;

    public SpawnModule() {
        super("spawn");
    }

    @Override
    public void load(Config config) {
        this.spawn = new Pos(config.getDouble("x"), config.getDouble("y"), config.getDouble("z"));

        this.instanceId = this.parseInstanceUuid(config.getString("instance-id"));

        // todo in the future just store the Instance. This would require doing so after the server has started (extensions are loaded, before players can join).
        Operadora.getEventNode().addListener(PlayerLoginEvent.class, event -> {
            InstanceManager instanceManager = MinecraftServer.getInstanceManager();
            Instance instance = instanceId == null ? null : instanceManager.getInstance(instanceId);
            if (instance == null) {
                if (instanceManager.getInstances().isEmpty()) {
                    Instance newInstance = instanceManager.createInstanceContainer();
                    newInstance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
                    instance = newInstance;
                } else {
                    instance = instanceManager.getInstances().iterator().next();
                }
            }

            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(spawn);
        });
    }

    private UUID parseInstanceUuid(String input) {
        if (input == null || input.isEmpty()) return null;
        if (!UUID_PATTERN.matcher(input).matches()) {
            LOGGER.warn("An spawning instance UUID was provided but is invalid ({})", input);
            return null;
        }

        return UUID.fromString(input);
    }

}
