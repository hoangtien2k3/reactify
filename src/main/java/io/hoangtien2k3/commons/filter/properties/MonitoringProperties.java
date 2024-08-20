package io.hoangtien2k3.commons.filter.properties;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringProperties {
    private boolean isEnable = true;
    private MeterRegistry meterRegistry = new LoggingMeterRegistry();
}
