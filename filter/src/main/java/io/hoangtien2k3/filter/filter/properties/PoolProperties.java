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
package io.hoangtien2k3.filter.filter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `PoolProperties` class encapsulates configuration settings for a
 * connection pool. It uses Lombok annotations to generate boilerplate code such
 * as getters, setters, and constructors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>maxSize</strong>: An integer representing the maximum number of
 * connections that the pool can hold. Defaults to `2000` if not explicitly
 * set.</li>
 * <li><strong>maxPendingAcquire</strong>: An integer representing the maximum
 * number of connections that can be pending acquisition from the pool. Defaults
 * to `2000` if not explicitly set.</li>
 * </ul>
 *
 * <h2>Lombok Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Automatically generates getters, setters,
 * `equals()`, `hashCode()`, and `toString()` methods.</li>
 * <li><strong>@AllArgsConstructor</strong>: Creates a constructor with all
 * fields as parameters, allowing for easy instantiation of the class with all
 * properties set.</li>
 * <li><strong>@NoArgsConstructor</strong>: Provides a no-argument constructor
 * for creating instances of the class with default values.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;ConfigurationProperties(prefix = "pool")
 * 	public class PoolConfig {
 *
 * 		private final PoolProperties poolProperties;
 *
 * 		&#64;Autowired
 * 		public PoolConfig(PoolProperties poolProperties) {
 * 			this.poolProperties = poolProperties;
 * 		}
 *
 * 		@PostConstruct
 * 		public void init() {
 * 			// Example of accessing properties
 * 			System.out.println("Max Pool Size: " + poolProperties.getMaxSize());
 * 			System.out.println("Max Pending Acquire: " + poolProperties.getMaxPendingAcquire());
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * pool:
 *   maxSize: 3000
 *   maxPendingAcquire: 5000
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `PoolProperties` class is used to configure the parameters for a
 * connection pool in an application. It allows you to specify the maximum size
 * of the pool and the maximum number of connections that can be pending
 * acquisition.
 * </p>
 *
 * <p>
 * The default value for `maxSize` is `2000`, which means the pool can hold up
 * to 2000 connections. If you want to allow more or fewer connections, you can
 * adjust this value.
 * </p>
 *
 * <p>
 * The `maxPendingAcquire` value is also set to `2000` by default. This property
 * defines the maximum number of connections that can be in the process of being
 * acquired from the pool. If the number of pending connections exceeds this
 * limit, additional requests will have to wait until a connection becomes
 * available.
 * </p>
 *
 * <p>
 * Both properties are customizable to fit the specific needs of your
 * application's connection pool. Adjusting these values can help optimize
 * performance and resource usage based on your application's requirements.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolProperties {
    private Integer maxSize = 2000;
    private Integer maxPendingAcquire = 2000;
}
