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
package io.hoangtien2k3.commons.filter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `ProxyProperties` class is used to configure proxy settings for an
 * application. It employs Lombok annotations to automatically generate getters,
 * setters, and constructors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>enable</strong>: A boolean flag indicating whether proxy support
 * is enabled. Defaults to `false` if not explicitly set.</li>
 * <li><strong>httpHost</strong>: A string representing the host address for
 * HTTP proxy configuration. This value should be set if an HTTP proxy is
 * required.</li>
 * <li><strong>httpPort</strong>: An integer specifying the port number for HTTP
 * proxy configuration. This value should be set in conjunction with
 * `httpHost`.</li>
 * <li><strong>httpsHost</strong>: A string representing the host address for
 * HTTPS proxy configuration. This value should be set if an HTTPS proxy is
 * required.</li>
 * <li><strong>httpsPort</strong>: An integer specifying the port number for
 * HTTPS proxy configuration. This value should be set in conjunction with
 * `httpsHost`.</li>
 * </ul>
 *
 * <h2>Lombok Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Automatically generates getters, setters,
 * `equals()`, `hashCode()`, and `toString()` methods for the class.</li>
 * <li><strong>@AllArgsConstructor</strong>: Creates a constructor with all
 * fields as parameters, allowing for easy instantiation with all properties
 * set.</li>
 * <li><strong>@NoArgsConstructor</strong>: Provides a no-argument constructor
 * for creating instances with default values.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;ConfigurationProperties(prefix = "proxy")
 * 	public class ProxyConfig {
 *
 * 		private final ProxyProperties proxyProperties;
 *
 * 		&#64;Autowired
 * 		public ProxyConfig(ProxyProperties proxyProperties) {
 * 			this.proxyProperties = proxyProperties;
 * 		}
 *
 * 		@PostConstruct
 * 		public void init() {
 * 			if (proxyProperties.isEnable()) {
 * 				// Example of accessing and using proxy settings
 * 				System.out.println("HTTP Proxy Host: " + proxyProperties.getHttpHost());
 * 				System.out.println("HTTP Proxy Port: " + proxyProperties.getHttpPort());
 * 				System.out.println("HTTPS Proxy Host: " + proxyProperties.getHttpsHost());
 * 				System.out.println("HTTPS Proxy Port: " + proxyProperties.getHttpsPort());
 * 			}
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * proxy:
 *   enable: true
 *   httpHost: "http-proxy.example.com"
 *   httpPort: 8080
 *   httpsHost: "https-proxy.example.com"
 *   httpsPort: 8443
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `ProxyProperties` class provides a way to configure proxy settings for
 * your application, including both HTTP and HTTPS proxies. It is particularly
 * useful when your application needs to communicate through a proxy server.
 * </p>
 *
 * <p>
 * By default, proxy support is disabled (i.e., `enable` is `false`). To use a
 * proxy, you need to set the `enable` flag to `true` and provide the necessary
 * `httpHost`, `httpPort`, `httpsHost`, and `httpsPort` values.
 * </p>
 *
 * <p>
 * The `httpHost` and `httpPort` define the proxy settings for HTTP connections,
 * while the `httpsHost` and `httpsPort` define the settings for HTTPS
 * connections. Make sure to configure these values appropriately based on your
 * network infrastructure and proxy server settings.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyProperties {
    private boolean enable = false;
    private String httpHost;
    private Integer httpPort;
    private String httpsHost;
    private Integer httpsPort;
}
