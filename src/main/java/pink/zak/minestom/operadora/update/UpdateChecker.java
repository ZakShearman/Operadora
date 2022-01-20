package pink.zak.minestom.operadora.update;

import com.google.gson.JsonObject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.utils.http.OperadoraBodyHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateChecker.class);
    private static final URI VERSION_URI = URI.create("https://operadora.zak.pink/api/v1/version");

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final OperadoraVersion runtimeVersion = Operadora.getMeta().version();

    public UpdateChecker() {
        this.checkForStartupUpdates();
        MinecraftServer.getSchedulerManager().buildTask(this::checkForUpdates)
            .delay(Duration.ofMinutes(30))
            .repeat(Duration.ofMinutes(30))
            .schedule();
    }

    private CompletableFuture<OperadoraVersion> retrieveVersion() {
        return this.httpClient.sendAsync(HttpRequest.newBuilder(VERSION_URI).GET().build(), OperadoraBodyHandler.ofJson())
            .thenApply(response -> {
                JsonObject content = response.body().getAsJsonObject();
                return new OperadoraVersion(content.get("commitHash").getAsString(), content.get("version").getAsString(), Integer.parseInt(content.get("buildNumber").getAsString()));
            });
    }

    private void checkForStartupUpdates() {
        this.retrieveVersion()
            .thenAccept(version -> {
                int versionsBehind = version.buildNumber() - this.runtimeVersion.buildNumber();
                if (versionsBehind > 0)
                    LOGGER.warn("There is a new Operadora version available ({} - #{}). You are running {} - #{}", version.version(), version.buildNumber(), this.runtimeVersion.version(), this.runtimeVersion.buildNumber());
            });
    }

    private void checkForUpdates() {
        this.retrieveVersion()
            .thenAccept(version -> {
                int versionsBehind = version.buildNumber() - this.runtimeVersion.buildNumber();
                if (versionsBehind > 0) {
                    Audience audience = Audiences.all(tAudience -> tAudience instanceof ConsoleSender || tAudience instanceof Player player && (player.hasPermission("operadora.updates") || player.getPermissionLevel() >= 4));
                    audience.sendMessage(
                        Component.text("There is a new Operadora version available (" + version.version() + " - #" + version.buildNumber() + ")." +
                            " You are running " + this.runtimeVersion.version() + " #" + this.runtimeVersion.buildNumber())
                    );
                }
            });
    }
}
