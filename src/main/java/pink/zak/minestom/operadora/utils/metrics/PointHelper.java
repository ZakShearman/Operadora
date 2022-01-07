package pink.zak.minestom.operadora.utils.metrics;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class PointHelper {

    public static Point now(String measurement, WritePrecision writePrecision) {
        return Point.measurement(measurement)
            .time(Instant.now(), writePrecision);
    }
}
