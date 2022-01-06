package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.monitoring.TickMonitor;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.InfluxMetric;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;

import java.time.Instant;
import java.util.List;

public class TickMonitorMetrics extends InfluxMetric {
    private static final String TICK_TIME_METRIC = "tick-time";
    private static final String ACQUISITION_TIME_METRIC = "acquisition-time";

    public TickMonitorMetrics(@NotNull InfluxMetricsModule module) {
        super(module);

        Operadora.getEventNode().addListener(ServerTickMonitorEvent.class, event -> {
            TickMonitor tickMonitor = event.getTickMonitor();

            module.writePoints(
                List.of(
                    Point.measurement(TICK_TIME_METRIC)
                        .addField("value", tickMonitor.getTickTime())
                        .time(Instant.now(), WritePrecision.MS),
                    Point.measurement(ACQUISITION_TIME_METRIC)
                        .addField("value", tickMonitor.getAcquisitionTime())
                        .time(Instant.now(), WritePrecision.MS)
                )
            );
        });
    }
}
