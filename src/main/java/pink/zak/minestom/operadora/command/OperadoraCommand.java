package pink.zak.minestom.operadora.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;
import pink.zak.minestom.operadora.module.ModuleManager;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class OperadoraCommand extends Command {
    private final ModuleManager moduleManager;

    public OperadoraCommand() {
        super("operadora");

        this.moduleManager = Operadora.getModuleManager();

        ArgumentLiteral moduleLiteral = ArgumentType.Literal("module");
        ArgumentLiteral listLiteral = ArgumentType.Literal("list");

        this.addSyntax(this::listModulesCommand, moduleLiteral, listLiteral);
    }

    private void listModulesCommand(CommandSender sender, CommandContext context) {
        Map<String, Module> moduleMap = this.moduleManager.getRegisteredModules();
        Collection<Module> enabledModules = this.moduleManager.getEnabledModules();

        if (sender instanceof Player) {
            sender.sendMessage(Component.text("-- There are " + enabledModules.size() + "/" + moduleMap.size() + " modules enabled --", NamedTextColor.GREEN));

            for (Module module : moduleMap.values()) {
                String status = module.isEnabled() ? "✓" : "✗";
                sender.sendMessage(Component.text("  [" + status + "] " + module.getId(), module.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED));
            }
        } else {
            sender.sendMessage("There are " + enabledModules.size() + "/" + moduleMap.size() + " modules enabled");

            if (enabledModules.size() > 0) {
                String enabledModulesList = enabledModules.stream()
                    .map(Module::getId)
                    .collect(Collectors.joining(", "));
                sender.sendMessage("Enabled modules: " + enabledModulesList);
            }
        }
    }
}
