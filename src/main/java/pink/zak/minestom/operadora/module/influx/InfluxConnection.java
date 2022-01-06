package pink.zak.minestom.operadora.module.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxConnection.class);
    private final @NotNull InfluxDBClient client;
    private final @NotNull WriteApi writeApi;

    public InfluxConnection(@NotNull String url, @NotNull String token, @NotNull String org, @NotNull String bucket) {
        this.client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        this.writeApi = this.client.makeWriteApi();
        LOGGER.info("Connected to InfluxDB ({}:{})", org, bucket);
    }

    public @NotNull WriteApi getWriteApi() {
        return this.writeApi;
    }

    public void close() {
        this.writeApi.close();
        this.client.close();
    }
}
