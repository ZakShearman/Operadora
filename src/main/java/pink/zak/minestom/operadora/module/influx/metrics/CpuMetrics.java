package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.sun.management.OperatingSystemMXBean;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.TimedInfluxMetric;
import pink.zak.minestom.operadora.utils.metrics.PointHelper;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

public class CpuMetrics extends TimedInfluxMetric {
    private static final OperatingSystemMXBean SYSTEM_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    private static final String MEASUREMENT = "process-load";

    public CpuMetrics(@NotNull InfluxMetricsModule module) {
        super(module, Duration.of(5, TimeUnit.SECOND));
    }

    @Override
    protected @NotNull List<Point> createPoints() {
        return List.of(
            PointHelper.now(MEASUREMENT, WritePrecision.S)
                .addField("value", SYSTEM_BEAN.getProcessCpuLoad())
        );
    }
}
