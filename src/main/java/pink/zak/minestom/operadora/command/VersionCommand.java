package pink.zak.minestom.operadora.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.config.OperadoraMeta;

import java.util.Arrays;

public class VersionCommand extends Command {
    private final OperadoraMeta meta = Operadora.getMeta();
    private final ExtensionManager extensionManager = MinecraftServer.getExtensionManager();

    public VersionCommand() {
        super("version", "ver", "v");

        ArgumentWord extensionArgument = ArgumentType.Word("extension").from(
            this.extensionManager.getExtensions().stream()
                .map(extension -> extension.getOrigin().getName())
                .toArray(String[]::new)
        );

        this.setDefaultExecutor(this::versionCommand);
        this.addSyntax(this::extensionVersionCommand, extensionArgument);
    }

    private void versionCommand(CommandSender sender, CommandContext context) {
        Component content = Component.text("Running Operadora version " + this.meta.version() + " (#" + this.meta.buildNumber() + " git-" + this.meta.shortCommitHash() + ")");
        sender.sendMessage(content);
    }

    private void extensionVersionCommand(CommandSender sender, CommandContext context) {
        String extensionName = context.get("extension");
        Extension extension = this.extensionManager.getExtension(extensionName);
        DiscoveredExtension origin = extension.getOrigin();

        JoinConfiguration joinConfiguration = JoinConfiguration.builder()
            .separator(Component.text(", ", NamedTextColor.GREEN))
            .build();

        Component content = Component.text(extensionName + " v" + origin.getVersion() + " ", NamedTextColor.GREEN);

        if (origin.getAuthors().length > 0)
            content = content
                .append(Component.text("\nAuthors: ", NamedTextColor.WHITE))
                .append(Component.join(joinConfiguration, Arrays.stream(origin.getAuthors()).map(Component::text).toArray(Component[]::new)));

        sender.sendMessage(content);
    }
}
