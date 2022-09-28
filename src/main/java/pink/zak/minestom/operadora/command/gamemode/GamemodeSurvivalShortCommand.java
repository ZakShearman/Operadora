package pink.zak.minestom.operadora.command.gamemode;

import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;

public class GamemodeSurvivalShortCommand extends Command {

    public GamemodeSurvivalShortCommand() {
        super("gms");

        //Upon invalid usage, print the correct usage of the command to the sender
        setDefaultExecutor((sender, context) -> {
            String commandName = context.getCommandName();

            sender.sendMessage(Component.text("Usage: /" + commandName + " <gamemode> [targets]", NamedTextColor.RED), MessageType.SYSTEM);
        });
    }
}
