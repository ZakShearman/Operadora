package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.TimedInfluxMetric;
import pink.zak.minestom.operadora.utils.metrics.PointHelper;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class InstanceMetrics extends TimedInfluxMetric {
    private static final InstanceManager INSTANCE_MANAGER = MinecraftServer.getInstanceManager();

    private static final String CHUNKS_LOADED_MEASUREMENT = "chunk-count";
    private static final String ENTITIES_LOADED_MEASUREMENT = "entity-count";

    public InstanceMetrics(@NotNull InfluxMetricsModule module) {
        super(module, Duration.of(5, TimeUnit.SECOND));
    }

    @Override
    protected @NotNull List<Point> createPoints() {
        int chunksLoaded = INSTANCE_MANAGER.getInstances()
                .stream()
                .map(Instance::getChunks)
                .map(Collection::size)
                .mapToInt(Integer::intValue).sum();

        int entitiesLoaded = INSTANCE_MANAGER.getInstances()
                .stream()
                .map(Instance::getEntities)
                .map(Collection::size)
                .mapToInt(Integer::intValue).sum();

        return List.of(
                PointHelper.now(CHUNKS_LOADED_MEASUREMENT, WritePrecision.S)
                        .addField("value", chunksLoaded),
                PointHelper.now(ENTITIES_LOADED_MEASUREMENT, WritePrecision.S)
                        .addField("value", entitiesLoaded)
        );
    }
}
