package pink.zak.minestom.operadora.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import pink.zak.minestom.operadora.Operadora;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ExtensionsCommand extends Command {
    private static final JoinConfiguration COMMA_JOIN_CONFIG = JoinConfiguration.commas(true);

    private final ExtensionManager extensionManager = MinecraftServer.getExtensionManager();

    public ExtensionsCommand() {
        super("extensions", "ex");

        this.addConditionalSyntax(Operadora.getOperatorRepository().getCommandCondition("operadora.command.extensions"), this::listExtensionsCommand);
    }

    private void listExtensionsCommand(CommandSender sender, CommandContext context) {
        Collection<Extension> extensions = this.extensionManager.getExtensions();


        Set<Component> extensionNames = new HashSet<>();

        for (Extension extension : extensions) {
            DiscoveredExtension extensionMeta = extension.getOrigin();
            String authorsText = extensionMeta.getAuthors().length < 1 ? "N/A" : String.join(", ", extensionMeta.getAuthors());
            String dependenciesText = extensionMeta.getAuthors().length < 1 ? "N/A" : String.join(", ", extensionMeta.getDependencies());
            Component hoverComponent = Component.text(
                    "Authors: " + authorsText
                            + "\nVersion: " + extensionMeta.getVersion()
                            + "\nDependencies: " + dependenciesText
            );

            Component component = Component.text().content(extensionMeta.getName()).hoverEvent(HoverEvent.showText(hoverComponent)).build();
            extensionNames.add(component);
        }

        sender.sendMessage(Component.text("Extensions (" + extensions.size() + "): ").append(Component.join(COMMA_JOIN_CONFIG, extensionNames)));
    }
}
