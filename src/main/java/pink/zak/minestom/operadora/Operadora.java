package pink.zak.minestom.operadora;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.command.OperadoraCommand;
import pink.zak.minestom.operadora.config.OperadoraConfig;
import pink.zak.minestom.operadora.module.ModuleManager;
import pink.zak.minestom.operadora.module.hostsupport.HostSupportModule;

import java.nio.file.Path;

public class Operadora {
    private static final Path BASE_PATH = Path.of("");
    private static final EventNode<Event> EVENT_NODE = EventNode.all("operadora");
    public static final Logger LOGGER = LoggerFactory.getLogger(Operadora.class);

    private static ModuleManager moduleManager;

    public static void main(String[] args) {
        OperadoraConfig config = new OperadoraConfig();
        config.applySystemProperties();

        MinecraftServer server = MinecraftServer.init();
        config.applyServerSettings();

        MinecraftServer.getGlobalEventHandler().addChild(EVENT_NODE);

        server.start(config.getIp(), config.getPort());

        moduleManager = new ModuleManager();

        HostSupportModule hostSupportModule = (HostSupportModule) moduleManager.getModule("host-support");
        if (hostSupportModule.isEnabled())
            hostSupportModule.onStart();

        registerCommands(
            new OperadoraCommand()
        );
    }

    private static void registerCommands(Command... commands) {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        for (Command command : commands)
            commandManager.register(command);
    }

    public static @NotNull Path getBasePath() {
        return BASE_PATH;
    }

    public static @NotNull EventNode<Event> getEventNode() {
        return EVENT_NODE;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
}
