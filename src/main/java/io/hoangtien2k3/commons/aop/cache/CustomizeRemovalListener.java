package io.hoangtien2k3.commons.aop.cache;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Log4j2
@AllArgsConstructor
public class CustomizeRemovalListener implements RemovalListener {
    private Method method;

    @Override
    public void onRemoval(@Nullable Object o, @Nullable Object o2, @NonNull RemovalCause removalCause) {
        if (removalCause.wasEvicted()) {
            log.info(
                    "Cache {}.{} was evicted because {}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    removalCause);
            Cache2LUtils.invokeMethod(method);
        }
    }
}
