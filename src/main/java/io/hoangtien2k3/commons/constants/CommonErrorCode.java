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
 * The `CommonErrorCode` class contains common error codes used throughout the
 * application. These error codes are defined as constant strings for easy
 * management and usage across the application.
 */
public class CommonErrorCode {

    /**
     * Error code for invalid requests.
     */
    public static final String BAD_REQUEST = "bad_request";

    /**
     * Error code when a resource is not found.
     */
    public static final String NOT_FOUND = "not_found";

    /**
     * Error code for invalid parameters.
     */
    public static final String INVALID_PARAMS = "invalid_params";

    /**
     * Error code when data already exists.
     */
    public static final String EXIST_DATA = "exist_data";

    /**
     * Error code when data does not exist.
     */
    public static final String NOT_EXIST_DATA = "not_exist_data";

    /**
     * Error code for internal server errors.
     */
    public static final String INTERNAL_SERVER_ERROR = "internal_error";

    /**
     * Error code when the user is not authorized.
     */
    public static final String UN_AUTHORIZATION = "un_auth";

    /**
     * Error code when the user does not have permission.
     */
    public static final String NO_PERMISSION = "no_permission";

    /**
     * Error code when access is denied.
     */
    public static final String ACCESS_DENIED = "access_denied";

    /**
     * Error code when there is an error parsing the token.
     */
    public static final String PARSE_TOKEN_ERROR = "parse_token_failed";

    /**
     * Error code for SQL-related errors.
     */
    public static final String SQL_ERROR = "sql";

    /**
     * Error code when group information is not found.
     */
    public static final String GROUP_INFO_NOT_FOUND = "group_info_not_found";

    /**
     * Error code indicating success.
     */
    public static final String SUCCESS = "success";

    /**
     * Error code when deserialization fails.
     */
    public static final String UN_DESERIALIZE = "un_deserialize";

    /**
     * Error code when there is a fault in password hashing.
     */
    public static final String HASHING_PASSWORD_FAULT = "hashing_password_fault";

    /**
     * Error code when an exception occurs during a SOAP call.
     */
    public static final String CALL_SOAP_ERROR = "Exception when call soap: ";
}
