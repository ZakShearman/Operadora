package pink.zak.minestom.operadora;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.config.OperadoraConfig;
import pink.zak.minestom.operadora.config.OperadoraMeta;
import pink.zak.minestom.operadora.manager.CommandLoader;
import pink.zak.minestom.operadora.module.ModuleManager;
import pink.zak.minestom.operadora.module.hostsupport.HostSupportModule;
import pink.zak.minestom.operadora.storage.OperatorRepository;
import pink.zak.minestom.operadora.storage.WhitelistRepository;
import pink.zak.minestom.operadora.update.UpdateChecker;

import java.nio.file.Path;

public class Operadora {
    private static final Path BASE_PATH = Path.of("");
    private static final EventNode<Event> EVENT_NODE = EventNode.all("operadora");
    private static final OperadoraConfig OPERADORA_CONFIG = OperadoraConfig.load();
    private static final OperadoraMeta OPERADORA_META = OperadoraMeta.load();

    private static OperatorRepository operatorRepository;
    private static WhitelistRepository whitelistRepository;

    private static ModuleManager moduleManager;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        OPERADORA_CONFIG.applySystemProperties();

        MinecraftServer server = MinecraftServer.init();
        OPERADORA_CONFIG.applyServerSettings();

        MinecraftServer.getGlobalEventHandler().addChild(EVENT_NODE);

        server.start(OPERADORA_CONFIG.ip(), OPERADORA_CONFIG.port());

        operatorRepository = new OperatorRepository();

        moduleManager = new ModuleManager();

        long startupTime = System.currentTimeMillis() - startTime;

        new UpdateChecker();

        HostSupportModule hostSupportModule = (HostSupportModule) moduleManager.getModule("host-support");
        if (hostSupportModule.isEnabled())
            hostSupportModule.onStart(startupTime);

        MinecraftServer.getSchedulerManager().buildShutdownTask(Operadora::onStop);

        new CommandLoader();
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

    public static OperadoraConfig getOperadoraConfig() {
        return OPERADORA_CONFIG;
    }

    public static OperadoraMeta getMeta() {
        return OPERADORA_META;
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
