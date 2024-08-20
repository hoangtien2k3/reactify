package io.hoangtien2k3.commons.filter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolProperties {
    private Integer maxSize = 2000;
    private Integer maxPendingAcquire = 2000;
}
