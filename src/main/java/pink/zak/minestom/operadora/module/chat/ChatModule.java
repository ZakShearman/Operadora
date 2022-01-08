package pink.zak.minestom.operadora.module.chat;

import com.typesafe.config.Config;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.Module;

public class ChatModule extends Module {
    private String format;

    public ChatModule() {
        super("chat");
    }

    @Override
    public void load(Config config) {
        this.format = config.getString("format");

        Operadora.getEventNode().addListener(PlayerChatEvent.class, event -> event.setChatFormat(this::formatChat));
    }

    private Component formatChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        // todo is there a better way to do this? I dont wanna parse everything every time, just the replaced stuff.
        TemplateResolver resolver = TemplateResolver.templates(
            Template.template("username", player.getUsername()),
            Template.template("display_name", player.getDisplayName() == null ? Component.empty() : player.getDisplayName()),
            Template.template("message", event.getMessage())
        );
        return MiniMessage.miniMessage().deserialize(this.format, resolver);
    }
}
