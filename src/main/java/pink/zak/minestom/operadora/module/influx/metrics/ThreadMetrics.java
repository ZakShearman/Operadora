package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.TimedInfluxMetric;
import pink.zak.minestom.operadora.utils.metrics.PointHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.util.List;

public class ThreadMetrics extends TimedInfluxMetric {
    private static final ThreadMXBean BEAN = ManagementFactory.getThreadMXBean();

    private static final String MEASUREMENT = "thread-count";

    public ThreadMetrics(@NotNull InfluxMetricsModule module) {
        super(module, Duration.of(5, TimeUnit.SECOND));
    }

    @Override
    protected @NotNull List<Point> createPoints() {
        return List.of(
            PointHelper.now(MEASUREMENT, WritePrecision.S)
                .addField("value", BEAN.getThreadCount())
        );
    }
}
