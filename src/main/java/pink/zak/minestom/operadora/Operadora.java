package pink.zak.minestom.operadora;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.config.OperadoraConfig;
import pink.zak.minestom.operadora.manager.CommandLoader;
import pink.zak.minestom.operadora.module.ModuleManager;
import pink.zak.minestom.operadora.module.hostsupport.HostSupportModule;
import pink.zak.minestom.operadora.storage.OperatorRepository;
import pink.zak.minestom.operadora.storage.WhitelistRepository;

import java.nio.file.Path;

public class Operadora {
    private static final Path BASE_PATH = Path.of("");
    private static final EventNode<Event> EVENT_NODE = EventNode.all("operadora");
    public static final Logger LOGGER = LoggerFactory.getLogger(Operadora.class);

    private static OperatorRepository operatorRepository;
    private static WhitelistRepository whitelistRepository;

    private static ModuleManager moduleManager;

    public static void main(String[] args) {
        OperadoraConfig config = new OperadoraConfig();
        config.applySystemProperties();

        MinecraftServer server = MinecraftServer.init();
        config.applyServerSettings();

        MinecraftServer.getGlobalEventHandler().addChild(EVENT_NODE);

        server.start(config.getIp(), config.getPort());

        operatorRepository = new OperatorRepository();

        moduleManager = new ModuleManager();

        HostSupportModule hostSupportModule = (HostSupportModule) moduleManager.getModule("host-support");
        if (hostSupportModule.isEnabled())
            hostSupportModule.onStart();

        MinecraftServer.getSchedulerManager().buildShutdownTask(Operadora::onStop);

        new CommandLoader();


        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer(); // TODO REMOVEEEE
        EVENT_NODE.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 100, 0));
            player.setAllowFlying(true);
            player.setFlying(true);
        });
    }

    private static void onStop() {
        operatorRepository.shutdown();
    }

    public static @NotNull
    Path getBasePath() {
        return BASE_PATH;
    }

    public static @NotNull
    EventNode<Event> getEventNode() {
        return EVENT_NODE;
    }

    public static OperatorRepository getOperatorRepository() {
        return operatorRepository;
    }

    public static WhitelistRepository getWhitelistRepository() {
        return whitelistRepository;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
}
