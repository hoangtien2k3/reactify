package io.hoangtien2k3.commons.aop.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CacheInfo {
    private Cache<Object, Object> cache;
    private GlobalCacheInfo globalCacheInfo;
}
