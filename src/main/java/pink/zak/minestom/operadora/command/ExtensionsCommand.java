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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtensionsCommand extends Command {
    private final ExtensionManager extensionManager = MinecraftServer.getExtensionManager();

    public ExtensionsCommand() {
        super("extensions", "ex");

        this.addConditionalSyntax(Operadora.getOperatorRepository().getCommandCondition(), this::listExtensionsCommand);
    }

    private void listExtensionsCommand(CommandSender sender, CommandContext context) {
        Collection<Extension> extensions = this.extensionManager.getExtensions();

        JoinConfiguration joinConfiguration = JoinConfiguration.builder()
            .separator(Component.text(", "))
            .build();
        Set<Component> extensionNames = new HashSet<>();

        for (Extension extension : extensions) {
            DiscoveredExtension extensionMeta = extension.getOrigin();
            Component hoverComponent = Component.text(
                "Authors: " + Arrays.stream(extensionMeta.getAuthors()).collect(Collectors.joining(", "))
                    + "\nVersion: " + extensionMeta.getVersion()
                    + "\nDependencies: " + Arrays.stream(extensionMeta.getDependencies()).collect(Collectors.joining(", ")));

            Component component = Component.text()
                .content(extensionMeta.getName())
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent))
                .build();

            extensionNames.add(component);
        }

        sender.sendMessage(Component.text("Extensions (" + extensions.size() + "): ").append(Component.join(joinConfiguration, extensionNames)));
    }
}
