package io.hoangtien2k3.commons.aop.cache.redis;

import java.util.concurrent.atomic.LongAdder;
import lombok.Data;

@Data
public class RedisCacheStats {
    private LongAdder hitCount = new LongAdder();
    private LongAdder missCount = new LongAdder();
}
