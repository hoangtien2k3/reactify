/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.filter.properties;

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
