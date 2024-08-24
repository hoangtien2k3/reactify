package io.hoangtien2k3.commons.aop.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GlobalCacheInfo {
    private boolean isOptional;
    private Class<?> type;
    private Class<?> wrapType;
    private Class<?> mapKeyType;
    private Class<?> mapValueType;
}
