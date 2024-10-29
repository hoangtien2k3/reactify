/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.reactify.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a method's result should be cached locally. This
 * can improve performance by avoiding redundant computations or repeated data
 * retrieval from external sources.
 *
 * <p>
 * The caching behavior is defined by the properties of the annotation:
 * <ul>
 * <li><strong>durationInMinute</strong>: The duration for which the cached
 * result is valid, specified in minutes.</li>
 * <li><strong>maxRecord</strong>: The maximum number of records that can be
 * stored in the cache.</li>
 * <li><strong>autoCache</strong>: A flag indicating whether caching should be
 * done automatically or manually.</li>
 * </ul>
 *
 * <p>
 * This annotation should be applied to methods that return a result that can
 * benefit from caching.
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>
 * &#64;LocalCache(durationInMinute = 10, maxRecord = 500, autoCache = true)
 * public MyObject fetchData(String param) {
 * 	// Method implementation that retrieves data
 * }
 * </pre>
 *
 * <h3>Annotation Properties:</h3>
 * <dl>
 * <dt>durationInMinute</dt>
 * <dd>The time period in minutes for which the cached data is valid. Default is
 * 120 minutes.</dd>
 *
 * <dt>maxRecord</dt>
 * <dd>The maximum number of entries that can be cached. Default is 1000
 * entries.</dd>
 *
 * <dt>autoCache</dt>
 * <dd>If set to true, the method result will be automatically cached. Default
 * is false.</dd>
 * </dl>
 *
 * <p>
 * This annotation is intended for use in performance-sensitive applications
 * where reducing latency and resource consumption is critical.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {
    int durationInMinute() default 120;

    int maxRecord() default 1000;

    boolean autoCache() default false;
}
