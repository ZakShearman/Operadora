package pink.zak.minestom.operadora.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import pink.zak.minestom.operadora.Operadora;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        this.addConditionalSyntax(Operadora.getOperatorRepository().getCommandCondition(), this::stopCommand);
    }

    private void stopCommand(CommandSender sender, CommandContext context) {
        MinecraftServer.stopCleanly();
    }
}
