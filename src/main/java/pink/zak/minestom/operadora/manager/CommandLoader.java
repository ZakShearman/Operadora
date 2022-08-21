package pink.zak.minestom.operadora.manager;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.command.ExtensionsCommand;
import pink.zak.minestom.operadora.command.OperadoraCommand;
import pink.zak.minestom.operadora.command.StopCommand;
import pink.zak.minestom.operadora.command.VersionCommand;
import pink.zak.minestom.operadora.command.gamemode.GamemodeCommand;
import pink.zak.minestom.operadora.command.operator.OperatorCommand;
import pink.zak.minestom.operadora.command.operator.RemoveOperatorCommand;
import pink.zak.minestom.operadora.utils.data.FileUtils;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

public class CommandLoader {
    private static final Path PATH = Operadora.getBasePath().resolve("commands.conf");
    private static final Map<String, Function<Config, Command>> AVAILABLE_COMMANDS;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLoader.class);

    static {
        AVAILABLE_COMMANDS = Map.ofEntries( // config is here for future features
                Map.entry("gamemode", config -> new GamemodeCommand()),
                Map.entry("operator", config -> new OperatorCommand()),
                Map.entry("remove-operator", config -> new RemoveOperatorCommand()),
                Map.entry("extensions", config -> new ExtensionsCommand()),
                Map.entry("operadora", config -> new OperadoraCommand()),
                Map.entry("stop", config -> new StopCommand()),
                Map.entry("version", config -> new VersionCommand())
        );
    }

    public CommandLoader() {
        FileUtils.saveResourceIfNotExists(PATH, "commands.conf");
        Config config = ConfigFactory.parseFile(PATH.toFile());

        CommandManager commandManager = MinecraftServer.getCommandManager();

        for (Map.Entry<String, Function<Config, Command>> entry : AVAILABLE_COMMANDS.entrySet()) {
            String id = entry.getKey();
            if (!config.hasPath(id)) {
                LOGGER.warn("Missing path for command " + id + ". Your commands.json may be broken or out of date.");
                continue;
            }
            Config subConfig = config.getConfig(id);
            if (subConfig.getBoolean("enabled")) {
                Command command = entry.getValue().apply(subConfig);
                commandManager.register(command);
            }
        }
    }
}
