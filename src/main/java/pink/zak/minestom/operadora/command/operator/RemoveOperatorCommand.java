package pink.zak.minestom.operadora.command.operator;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import pink.zak.minestom.operadora.Operadora;

public class RemoveOperatorCommand extends Command {

    public RemoveOperatorCommand() {
        super("deop");

        this.addConditionalSyntax(Operadora.getOperatorRepository().getCommandCondition(), this::removeOperatorCommand);
    }

    private void removeOperatorCommand(CommandSender sender, CommandContext context) {

    }
}
