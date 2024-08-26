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
 * The `KeyCloakProperties` class is used to store configuration properties
 * related to Keycloak. It is a simple POJO (Plain Old Java Object) with Lombok
 * annotations to reduce boilerplate code such as getters, setters, and
 * constructors.
 *
 * <h2>Attributes:</h2>
 * <ul>
 * <li><strong>clientId</strong>: A string representing the client ID used in
 * Keycloak. This is typically used to identify the client application.</li>
 * <li><strong>clientSecret</strong>: A string representing the client secret
 * used in Keycloak. This secret is used to authenticate the client application
 * with Keycloak.</li>
 * </ul>
 *
 * <h2>Lombok Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Generates boilerplate code such as getters,
 * setters, `equals()`, `hashCode()`, and `toString()` methods
 * automatically.</li>
 * <li><strong>@AllArgsConstructor</strong>: Generates a constructor with all
 * fields as parameters. This allows you to create an instance of the class with
 * all attributes set at once.</li>
 * <li><strong>@NoArgsConstructor</strong>: Generates a no-arguments
 * constructor. This is useful for creating instances of the class without
 * setting any attributes initially.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;Configuration
 * 	&#64;ConfigurationProperties(prefix = "keycloak")
 * 	public class KeyCloakConfig {
 *
 * 		private final KeyCloakProperties keyCloakProperties;
 *
 * 		&#64;Autowired
 * 		public KeyCloakConfig(KeyCloakProperties keyCloakProperties) {
 * 			this.keyCloakProperties = keyCloakProperties;
 * 		}
 *
 * 		@PostConstruct
 * 		public void init() {
 * 			// Example of accessing properties
 * 			System.out.println("Client ID: " + keyCloakProperties.getClientId());
 * 			System.out.println("Client Secret: " + keyCloakProperties.getClientSecret());
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <h2>Configuration Example:</h2>
 *
 * <pre>{@code
 * # application.yml
 * keycloak:
 *   clientId: my-client-id
 *   clientSecret: my-client-secret
 * }</pre>
 *
 * In this example, the `KeyCloakProperties` class is used to bind the
 * configuration properties from `application.yml` file. The `KeyCloakConfig`
 * class demonstrates how to use these properties within a Spring Boot
 * application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCloakProperties {
    private String clientId;
    private String clientSecret;
}
