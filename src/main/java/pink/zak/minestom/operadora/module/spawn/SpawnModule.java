package pink.zak.minestom.operadora.module.spawn;

import java.util.UUID;
import java.util.regex.Pattern;

import com.typesafe.config.Config;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;

public class SpawnModule extends Module {

	private Pos spawn;
	private UUID instanceId;

	public SpawnModule() {
		super("spawn");
	}

	@Override
	public void load(Config config) {
		this.spawn = new Pos(config.getInt("x"), config.getInt("y"), config.getInt("z"));
		this.instanceId = config.hasPath("world")
				&& Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
						.matcher(config.getString("world")).matches() ? UUID.fromString(config.getString("world"))
								: null;

		Operadora.getEventNode().addListener(PlayerLoginEvent.class, event -> {
			InstanceManager im = MinecraftServer.getInstanceManager();
			Instance instance = instanceId == null ? null : im.getInstance(instanceId);
			if (instance == null) {
				if (im.getInstances().isEmpty()) {
					Instance i = im.createInstanceContainer();
					i.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
					event.setSpawningInstance(i);
				} else
					event.setSpawningInstance(im.getInstances().iterator().next());
			} else
				event.setSpawningInstance(instance);
			event.getPlayer().setRespawnPoint(spawn);
		});
	}

}
