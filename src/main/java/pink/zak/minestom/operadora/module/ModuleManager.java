package pink.zak.minestom.operadora.module;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.chat.ChatModule;
import pink.zak.minestom.operadora.module.hostsupport.HostSupportModule;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.utils.data.FileUtils;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleManager.class);
    private final Map<String, Module> registeredModules = new HashMap<>();

    public ModuleManager() {
        this.createModules();

        Path path = Operadora.getBasePath().resolve("modules.conf");
        FileUtils.saveResourceIfNotExists(path, "modules.conf");
        Config config = ConfigFactory.parseFile(path.toFile());

        for (Map.Entry<String, ConfigValue> entry : config.root().entrySet()) {
            String moduleId = entry.getKey();

            if (entry.getValue() instanceof ConfigObject moduleConfigObject) {
                Config moduleConfig = moduleConfigObject.toConfig();
                Module module = this.registeredModules.get(moduleId);
                if (module == null) {
                    LOGGER.warn("Unknown module {} was specified in modules.conf", moduleId);
                    continue;
                }

                if (moduleConfig.getBoolean("enabled")) {
                    module.load(moduleConfig);
                    module.setEnabled(true);
                    LOGGER.debug("Loaded module {}", moduleId);
                }
            } else {
                LOGGER.warn("Unknown value specified in modules.conf ({})", moduleId);
            }
        }
    }

    private void createModules() {
        this.registeredModules.put("chat", new ChatModule());
        this.registeredModules.put("host-support", new HostSupportModule());
        this.registeredModules.put("influx-metrics", new InfluxMetricsModule());
    }

    public Map<String, Module> getRegisteredModules() {
        return this.registeredModules;
    }

    public Collection<Module> getEnabledModules() {
        return this.registeredModules
            .values()
            .stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toUnmodifiableSet());
    }

    public Module getModule(String id) {
        return this.registeredModules.get(id);
    }
}
