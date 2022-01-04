package pink.zak.minestom.operadora.module;

import com.typesafe.config.Config;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class Module {
    private final @NotNull String id;
    private boolean enabled = false;

    public Module(@NotNull String id) {
        this.id = id;
    }

    public @NotNull String getId() {
        return this.id;
    }

    @ApiStatus.Internal
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public abstract void load(Config config);
}
