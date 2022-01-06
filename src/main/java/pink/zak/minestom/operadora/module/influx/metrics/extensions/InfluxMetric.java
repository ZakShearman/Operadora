package pink.zak.minestom.operadora.module.influx.metrics.extensions;

import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;

public abstract class InfluxMetric {
    protected final @NotNull InfluxMetricsModule module;

    protected InfluxMetric(@NotNull InfluxMetricsModule module) {
        this.module = module;
    }
}
