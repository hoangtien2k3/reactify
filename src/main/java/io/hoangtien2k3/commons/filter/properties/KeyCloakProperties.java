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
