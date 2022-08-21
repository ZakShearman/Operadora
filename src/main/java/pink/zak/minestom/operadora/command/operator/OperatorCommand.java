package pink.zak.minestom.operadora.command.operator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.storage.OperatorRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// note this is planned for removal once LuckPerms officially supports Minestom
public class OperatorCommand extends Command {
    OperatorRepository operatorRepository = Operadora.getOperatorRepository();

    public OperatorCommand() {
        super("op");

        ArgumentLiteral listArgument = ArgumentType.Literal("list");
        ArgumentEntity playerArgument = ArgumentType.Entity("target").onlyPlayers(true).singleEntity(true);

        CommandCondition commandCondition = this.operatorRepository.getCommandCondition("operadora.command.op");
        this.addConditionalSyntax(commandCondition, this::opPlayerCommand, playerArgument);
        this.addConditionalSyntax(commandCondition, (sender, context) -> {
        }, listArgument);
    }

    private void opPlayerCommand(CommandSender sender, CommandContext context) {
        EntityFinder entityFinder = context.get("target");
        Player target = entityFinder.findFirstPlayer(sender);

        if (target == null && context.getRaw("target").equals("list")) {
            this.listOpsCommand(sender);
            return;
        }

        if (this.operatorRepository.addOperator(target.getUuid()))
            sender.sendMessage(Component.text("Added " + target.getUsername() + " as an operator.", NamedTextColor.GREEN));
        else
            sender.sendMessage(Component.text(target.getUsername() + " is already an operator.", NamedTextColor.RED));
    }

    private void listOpsCommand(CommandSender sender) {
        Set<UUID> operators = this.operatorRepository.getOperatorUuids();
        sender.sendMessage("Operators (" + operators.size() + "): " + operators.stream().map(UUID::toString).collect(Collectors.joining(", ")));
    }
}
