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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `UserDTO` class represents a Data Transfer Object (DTO) used for
 * user-related information. It is typically used to encapsulate user data
 * obtained from authentication or authorization services.
 *
 * <h2>Annotations:</h2>
 * <ul>
 * <li><strong>@Data</strong>: Lombok annotation that generates getters,
 * setters, equals, hashCode, and toString methods, as well as a constructor
 * with all arguments and a no-args constructor.</li>
 * <li><strong>@NoArgsConstructor</strong>: Lombok annotation that generates a
 * no-arguments constructor.</li>
 * <li><strong>@AllArgsConstructor</strong>: Lombok annotation that generates a
 * constructor with all fields as arguments.</li>
 * <li><strong>@JsonProperty</strong>: Jackson annotation used to map JSON
 * properties to Java fields, ensuring the correct mapping between JSON data and
 * the DTO fields.</li>
 * </ul>
 *
 * <h2>Fields:</h2>
 * <ul>
 * <li><strong>id</strong>: The unique identifier of the user, mapped from the
 * JSON property "sub".</li>
 * <li><strong>username</strong>: The preferred username of the user, mapped
 * from the JSON property "preferred_username".</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * // Creating a UserDTO instance using the all-args constructor
 * UserDTO user = new UserDTO("12345", "tien");
 *
 * // Accessing properties
 * String userId = user.getId();
 * String username = user.getUsername();
 *
 * // Displaying user information
 * System.out.println("User ID: " + user.getId());
 * System.out.println("Username: " + user.getUsername());
 *
 * // Creating a UserDTO instance using the no-args constructor and setters
 * UserDTO anotherUser = new UserDTO();
 * anotherUser.setId("67890");
 * anotherUser.setUsername("hoangtien2k3");
 * }</pre>
 *
 * <h2>Detailed Description:</h2>
 *
 * <p>
 * The `UserDTO` class is a simple data structure used to transfer user
 * information between different layers of an application or between systems. It
 * includes fields for a unique user ID and a preferred username.
 * </p>
 *
 * <p>
 * The class is annotated with Lombok annotations to simplify the creation and
 * manipulation of `UserDTO` instances. The `@JsonProperty` annotations are used
 * to ensure that JSON properties are correctly mapped to the Java fields, which
 * is particularly useful when working with JSON data from external services or
 * APIs.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty("sub")
    private String id;

    @JsonProperty("preferred_username")
    private String username;
}
