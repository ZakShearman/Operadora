package pink.zak.minestom.operadora.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.time.TimeUnit;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.utils.data.BasicJsonRepository;
import pink.zak.minestom.operadora.utils.data.FileConverterUtils;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WhitelistRepository extends BasicJsonRepository {
    private final Set<UUID> whitelistedUuids = new HashSet<>();
    private final Task task;
    private boolean changeMade;

    public WhitelistRepository() {
        super(Operadora.getBasePath().resolve("data").resolve("whitelist.json"));
        this.load();

        // we should save every so often if a change is made
        this.task = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    if (this.changeMade)
                        this.save();
                })
                .delay(5, TimeUnit.MINUTE)
                .repeat(5, TimeUnit.MINUTE)
                .schedule();
    }

    private void load() {
        if (!Files.exists(super.path))
            return;

        JsonArray jsonArray = FileConverterUtils.getAsJson(super.path.toFile()).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray)
            this.whitelistedUuids.add(UUID.fromString(jsonElement.getAsString()));

        LOGGER.debug("Loaded " + this.whitelistedUuids.size() + " whitelisted players");
    }

    private void save() {
        JsonArray jsonArray = new JsonArray();
        for (UUID uuid : this.whitelistedUuids)
            jsonArray.add(uuid.toString());

        super.save(jsonArray);
    }

    public void shutdown() {
        this.task.cancel();
    }

    public Set<UUID> getWhitelistedUuids() {
        return Set.copyOf(this.whitelistedUuids);
    }

    public boolean addWhitelist(UUID uuid) {
        this.changeMade = true;
        return this.whitelistedUuids.add(uuid);
    }

    public boolean removeWhitelist(UUID uuid) {
        this.changeMade = true;
        return this.whitelistedUuids.remove(uuid);
    }

    public boolean isWhitelisted(UUID uuid) {
        return this.whitelistedUuids.contains(uuid);
    }
}
