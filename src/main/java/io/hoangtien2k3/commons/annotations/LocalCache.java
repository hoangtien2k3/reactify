package io.hoangtien2k3.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {
    int durationInMinute() default 120;

    int maxRecord() default 1000;

    boolean autoCache() default false;
}
