package pink.zak.minestom.operadora.module.influx;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import com.typesafe.config.Config;

import pink.zak.minestom.operadora.module.Module;
import pink.zak.minestom.operadora.module.influx.metrics.CpuMetrics;
import pink.zak.minestom.operadora.module.influx.metrics.InstanceMetrics;
import pink.zak.minestom.operadora.module.influx.metrics.MemoryMetrics;
import pink.zak.minestom.operadora.module.influx.metrics.PlayerCountMetric;
import pink.zak.minestom.operadora.module.influx.metrics.ThreadMetrics;
import pink.zak.minestom.operadora.module.influx.metrics.TickMonitorMetrics;
import pink.zak.minestom.operadora.module.influx.metrics.extensions.InfluxMetric;

public class InfluxMetricsModule extends Module {
    private final Set<InfluxMetric> activeMetrics = new HashSet<>();
    private InfluxConnection influxConnection;
    private WriteApi writeApi;

    public InfluxMetricsModule() {
        super("influx-metrics");
    }

    @Override
    public void load(Config config) {
        String url = config.getString("url");
        String token = config.getString("token");
        String organisation = config.getString("organisation");
        String bucket = config.getString("bucket");

        this.influxConnection = new InfluxConnection(url, token, organisation, bucket);
        this.writeApi = this.influxConnection.getWriteApi();

        this.createMetrics();
    }

    private void createMetrics() {
        this.activeMetrics.add(new CpuMetrics(this));
        this.activeMetrics.add(new InstanceMetrics(this));
        this.activeMetrics.add(new MemoryMetrics(this));
        this.activeMetrics.add(new PlayerCountMetric(this));
        this.activeMetrics.add(new ThreadMetrics(this));
        this.activeMetrics.add(new TickMonitorMetrics(this));
    }

    public void writePoints(List<Point> points) {
        this.writeApi.writePoints(points);
    }
}
