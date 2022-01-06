package pink.zak.minestom.operadora.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.utils.data.BasicJsonRepository;
import pink.zak.minestom.operadora.utils.data.FileConverterUtils;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// Yes op levels aren't supported, how many people actually use them?
// I'll add them if compelled to by some force
public class OperatorRepository extends BasicJsonRepository {
    private final Set<UUID> operatorUuids = new HashSet<>();
    private final Task task;
    private boolean changeMade;

    public OperatorRepository() {
        super(Operadora.getBasePath().resolve("data").resolve("operators.json"));
        this.load();

        // we should save every so often if a change is made
        this.task = MinecraftServer.getSchedulerManager().buildTask(() -> {
                if (this.changeMade)
                    this.save();
            })
            .delay(5, TimeUnit.MINUTE)
            .repeat(5, TimeUnit.MINUTE)
            .schedule();

        Operadora.getEventNode().addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            if (this.operatorUuids.contains(player.getUuid()))
                player.setPermissionLevel(4);
        });
    }

    private void load() {
        if (!Files.exists(super.path))
            return;

        JsonArray jsonArray = FileConverterUtils.getAsJson(super.path.toFile()).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray)
            this.operatorUuids.add(UUID.fromString(jsonElement.getAsString()));

        LOGGER.debug("Loaded " + this.operatorUuids.size() + " operators");
    }

    private void save() {
        JsonArray jsonArray = new JsonArray();
        for (UUID uuid : this.operatorUuids)
            jsonArray.add(uuid.toString());

        super.save(jsonArray);
    }

    public void shutdown() {
        this.task.cancel();
    }

    public Set<UUID> getOperatorUuids() {
        return Set.copyOf(this.operatorUuids);
    }

    public boolean addOperator(UUID uuid) {
        this.changeMade = true;
        return this.operatorUuids.add(uuid);
    }

    public boolean removeOperator(UUID uuid) {
        this.changeMade = true;
        return this.operatorUuids.remove(uuid);
    }

    public boolean isOperator(UUID uuid) {
        return this.operatorUuids.contains(uuid);
    }

    public CommandCondition getCommandCondition(String permission) {
        return (sender, commandString) -> {
            if (!(sender instanceof Player) || ((Player) sender).getPermissionLevel() == 4)
                return true;
            if (commandString != null)
                sender.sendMessage(Component.text("Insufficient permissions.", NamedTextColor.RED));
            return false;
        };
    }
}
