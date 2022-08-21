package pink.zak.minestom.operadora.module.influx.metrics.extensions;

import com.influxdb.client.write.Point;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.operadora.module.influx.InfluxMetricsModule;

import java.time.Duration;
import java.util.List;

public abstract class TimedInfluxMetric extends InfluxMetric {
    private final @NotNull Task task;

    protected TimedInfluxMetric(@NotNull InfluxMetricsModule module, @NotNull Duration fetchDelay) {
        super(module);
        this.task = MinecraftServer.getSchedulerManager()
                .buildTask(() -> super.module.writePoints(this.createPoints()))
                .repeat(fetchDelay)
                .schedule();
    }

    protected abstract @NotNull List<Point> createPoints();

    public void stop() {
        this.task.cancel();
    }
}
