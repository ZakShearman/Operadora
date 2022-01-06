package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import net.minestom.server.MinecraftServer;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.TimedInfluxMetric;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class PlayerCountMetric extends TimedInfluxMetric {
    private static final String MEASUREMENT = "player-count";

    public PlayerCountMetric(InfluxMetricsModule module) {
        super(module, Duration.of(10, TimeUnit.SECOND));
    }

    @Override
    protected @NotNull List<Point> createPoints() {
        return List.of(
            Point.measurement(MEASUREMENT)
            .addField("value", MinecraftServer.getConnectionManager().getOnlinePlayers().size())
            .time(Instant.now(), WritePrecision.S)
        );
    }
}
