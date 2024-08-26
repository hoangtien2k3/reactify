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
