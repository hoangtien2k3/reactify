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
package io.hoangtien2k3.commons.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `TokenUser` class represents a user token with various properties related
 * to user identity and organization. It is used to encapsulate user information
 * typically obtained from an authentication system or token service.
 *
 * <h2>Annotations:</h2>
 * <ul>
 * <li><strong>@JsonIgnoreProperties</strong>: Specifies that any properties not
 * bound to this instance are ignored during deserialization. This helps handle
 * cases where the JSON data contains extra properties not defined in the
 * `TokenUser` class.</li>
 * <li><strong>@Data</strong>: Lombok annotation that generates getters,
 * setters, equals, hashCode, and toString methods, as well as a constructor
 * with all arguments and a no-args constructor.</li>
 * <li><strong>@AllArgsConstructor</strong>: Lombok annotation that generates a
 * constructor with all fields as arguments.</li>
 * <li><strong>@NoArgsConstructor</strong>: Lombok annotation that generates a
 * no-arguments constructor.</li>
 * <li><strong>@Builder</strong>: Lombok annotation that generates a builder
 * pattern for the class, allowing fluent and immutable object creation.</li>
 * </ul>
 *
 * <h2>Fields:</h2>
 * <ul>
 * <li><strong>id</strong>: A unique identifier for the user.</li>
 * <li><strong>name</strong>: The full name of the user.</li>
 * <li><strong>username</strong>: The username of the user, typically used for
 * login.</li>
 * <li><strong>email</strong>: The email address of the user.</li>
 * <li><strong>individualId</strong>: An identifier for the individual
 * associated with the user. Annotated with @JsonProperty("individual_id") to
 * map the JSON property "individual_id" to this field.</li>
 * <li><strong>organizationId</strong>: An identifier for the organization
 * associated with the user.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * // Creating a TokenUser instance using the builder pattern
 * TokenUser user = TokenUser.builder().id("12345").name("Hoang Tien").username("hoangtien2k3")
 * 		.email("hoangtien2k3@gmail.com").individualId("ind-67890").organizationId("org-123").build();
 *
 * // Accessing properties
 * String userId = user.getId();
 * String userEmail = user.getEmail();
 *
 * // Displaying user information
 * System.out.println("User Name: " + user.getName());
 * System.out.println("User Email: " + user.getEmail());
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `TokenUser` class serves as a data model for holding user information
 * extracted from authentication tokens or identity services. It includes fields
 * for user identification, personal details, and organizational information.
 * The class is annotated with Lombok annotations to simplify the creation and
 * manipulation of `TokenUser` instances.
 * </p>
 *
 * <p>
 * With the `@JsonIgnoreProperties` annotation, the class can handle extra
 * properties in JSON data gracefully. The `@JsonProperty("individual_id")`
 * annotation ensures that the JSON field "individual_id" is correctly mapped to
 * the `individualId` field in the class.
 * </p>
 */
@JsonIgnoreProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenUser {
    private String id;
    private String name;
    private String username;
    private String email;

    @JsonProperty("individual_id")
    private String individualId;

    private String organizationId;
}
