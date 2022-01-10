package pink.zak.minestom.operadora.module.hostsupport;

import com.typesafe.config.Config;
import net.kyori.adventure.text.Component;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;

import java.text.DecimalFormat;

public class HostSupportModule extends Module {

    public HostSupportModule() {
        super("host-support");
    }

    @Override
    public void load(Config config) {
        boolean showIp = config.getBoolean("show-ip");
        Operadora.getEventNode()
            .addListener(PlayerSpawnEvent.class, event -> { // don't use login or the disconnect won't be fired if they didn't log in successfully
                Player player = event.getPlayer();
                String ip = showIp ? player.getPlayerConnection().getServerAddress() : "hidden";
                Audiences.console().sendMessage(Component.text(player.getUsername() + " [/" + ip + "] logged in logged in with entity id " + player.getEntityId()));
            })
            .addListener(PlayerDisconnectEvent.class, event -> {
                Audiences.console().sendMessage(Component.text(event.getPlayer().getUsername() + " lost connection: N/A"));
            });
    }

    public void onStart(long startupMillis) {
        String formattedTime = new DecimalFormat("#.##").format(startupMillis / 1000d);
        Audiences.console().sendMessage(Component.text("Done (" + formattedTime + "s)! For help, type \"help\" or \"?\""));
    }
}
