package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.monitoring.TickMonitor;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.Operadora;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.InfluxMetric;
import pink.zak.minestom.operadora.utils.metrics.PointHelper;

import java.util.List;

public class TickMonitorMetrics extends InfluxMetric {
    private static final String TICK_TIME_MEASUREMENT = "tick-time";
    private static final String ACQUISITION_TIME_MEASUREMENT = "acquisition-time";

    public TickMonitorMetrics(@NotNull InfluxMetricsModule module) {
        super(module);

        Operadora.getEventNode().addListener(ServerTickMonitorEvent.class, event -> {
            TickMonitor tickMonitor = event.getTickMonitor();

            module.writePoints(
                    List.of(
                            PointHelper.now(TICK_TIME_MEASUREMENT, WritePrecision.MS)
                                    .addField("value", tickMonitor.getTickTime()),
                            PointHelper.now(ACQUISITION_TIME_MEASUREMENT, WritePrecision.MS)
                                    .addField("value", tickMonitor.getAcquisitionTime())
                    )
            );
        });
    }
}
