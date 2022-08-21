package pink.zak.minestom.operadora.module.influx.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.TimedInfluxMetric;
import pink.zak.minestom.operadora.utils.metrics.PointHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.Duration;
import java.util.List;

public class MemoryMetrics extends TimedInfluxMetric {
    // Swap seemed to be buggy so it's commented out
    //private static final OperatingSystemMXBean SYSTEM_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private static final MemoryMXBean MEMORY_BEAN = ManagementFactory.getMemoryMXBean();

    private static final String MEMORY_ALLOCATED_MEASUREMENT = "allocated-memory";
    private static final String MEMORY_USED_MEASUREMENT = "used-memory";

    public MemoryMetrics(@NotNull InfluxMetricsModule module) {
        super(module, Duration.of(5, TimeUnit.SECOND));
    }

    @Override
    protected @NotNull List<Point> createPoints() {
        MemoryUsage heapUsage = MEMORY_BEAN.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = MEMORY_BEAN.getNonHeapMemoryUsage();
        return List.of(
                PointHelper.now(MEMORY_ALLOCATED_MEASUREMENT, WritePrecision.S)
                        .addField("heap", heapUsage.getCommitted())
                        .addField("non-heap", nonHeapUsage.getCommitted()),
                //.addField("swap", SYSTEM_BEAN.getTotalSwapSpaceSize()),
                PointHelper.now(MEMORY_USED_MEASUREMENT, WritePrecision.S)
                        .addField("heap", heapUsage.getUsed())
                        .addField("non-heap", nonHeapUsage.getUsed())
                //.addField("swap", SYSTEM_BEAN.getTotalSwapSpaceSize() - SYSTEM_BEAN.getFreeSwapSpaceSize())
        );
    }
}
