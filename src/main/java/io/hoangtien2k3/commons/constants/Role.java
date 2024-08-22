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
package io.hoangtien2k3.commons.constants;

/**
 * Enumeration representing different user roles in the application.
 * <p>
 * This enum is used to define and manage various user roles and their
 * associated permissions.
 * </p>
 * <p>
 * Each constant in this enum corresponds to a specific role that a user can
 * have within the system.
 * </p>
 */
public enum Role {
    /**
     * Represents an administrative role with the highest level of permissions.
     * <p>
     * Users with this role typically have full access to all system resources and
     * management capabilities.
     * </p>
     */
    ROLE_admin,

    /**
     * Represents a standard user role with limited permissions.
     * <p>
     * Users with this role typically have access to basic functionalities and
     * resources, but not administrative controls.
     * </p>
     */
    ROLE_user
}
