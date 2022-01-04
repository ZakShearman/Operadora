package pink.zak.minestom.operadora.module.hostsupport;

import com.typesafe.config.Config;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;

public class HostSupportModule extends Module {

    public HostSupportModule() {
        super("host-support");
    }

    @Override
    public void load(Config config) {
        boolean showIp = config.getBoolean("show-ip");
        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer(); // TODO REMOVEEEE
        Operadora.getEventNode()
            .addListener(PlayerLoginEvent.class, event -> {
                Player player = event.getPlayer();
                event.setSpawningInstance(instance);
                player.setRespawnPoint(new Pos(0, 100, 0));
                player.setAllowFlying(true);
                player.setFlying(true);
            })
            .addListener(PlayerSpawnEvent.class, event -> { // don't use login or the disconnect won't be fired if they didn't log in successfully
                Player player = event.getPlayer();
                String ip = showIp ? player.getPlayerConnection().getServerAddress() : "hidden";
                Audiences.console().sendMessage(Component.text(player.getUsername() + " [/" + ip + "] logged in logged in with entity id " + player.getEntityId()));
            })
            .addListener(PlayerDisconnectEvent.class, event -> {
                Audiences.console().sendMessage(Component.text(event.getPlayer().getUsername() + " lost connection: N/A"));
            });
    }

    public void onStart() {
        Audiences.console().sendMessage(Component.text("Done - Started Up"));
    }
}
