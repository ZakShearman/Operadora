package pink.zak.minestom.operadora.command.operator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.storage.OperatorRepository;

public class RemoveOperatorCommand extends Command {
    private final OperatorRepository operatorRepository = Operadora.getOperatorRepository();

    public RemoveOperatorCommand() {
        super("deop");

        ArgumentEntity playerArgument = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true);
        this.addConditionalSyntax(Operadora.getOperatorRepository().getCommandCondition("operadora.command.deop"), this::removeOperatorCommand, playerArgument);
    }

    private void removeOperatorCommand(CommandSender sender, CommandContext context) {
        EntityFinder entityFinder = context.get("target");
        Player target = entityFinder.findFirstPlayer(sender);

        if (this.operatorRepository.removeOperator(target.getUuid()))
            sender.sendMessage(Component.text(target.getUsername() + " is no longer an operator.", NamedTextColor.GREEN));
        else
            sender.sendMessage(Component.text(target.getUsername() + " is not an operator.", NamedTextColor.RED));
    }
}
