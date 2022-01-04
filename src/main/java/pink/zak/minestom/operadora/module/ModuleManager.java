package pink.zak.minestom.operadora.module;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.hostsupport.HostSupportModule;
import pink.zak.minestom.operadora.utils.data.FileUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleManager.class);
    private final Map<String, Module> registeredModules = new HashMap<>();

    public ModuleManager() {
        this.createModules();

        Path path = Operadora.getBasePath().resolve("modules.conf");
        FileUtils.saveResourceIfNotExists(path, "modules.conf");
        Config config = ConfigFactory.parseFile(path.toFile());

        System.out.println("A");
        for (Map.Entry<String, ConfigValue> entry : config.root().entrySet()) {
            System.out.println("B " + entry);
            String moduleId = entry.getKey();

            if (entry.getValue() instanceof ConfigObject moduleConfigObject) {
                Config moduleConfig = moduleConfigObject.toConfig();
                System.out.println("C");
                Module module = this.registeredModules.get(moduleId);
                if (module == null) {
                    LOGGER.warn("Unknown module {} was specified in modules.conf", moduleId);
                    continue;
                }

                if (moduleConfig.getBoolean("enabled")) {
                    module.load(moduleConfig);
                    LOGGER.info("Loaded module {}", moduleId);
                }
            } else {
                LOGGER.warn("Unknown value specified in modules.conf ({})", moduleId);
            }
        }
    }

    private void createModules() {
        this.registeredModules.put("host-support", new HostSupportModule());
    }

    public Module getModule(String id) {
        return this.registeredModules.get(id);
    }
}
