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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.springframework.http.MediaType;

/**
 * The Constants class contains various constants used throughout the
 * application. These constants include regular expression patterns,
 * configuration related to security, date and time formats, and other specific
 * data type names.
 */
public final class Constants {

    /**
     * Regular expression pattern for validating names.
     */
    public static final String NAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌọÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";

    /**
     * Regular expression pattern for validating email addresses.
     */
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    /**
     * Regular expression pattern for validating dates in the format dd/MM/yyyy.
     */
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";

    /**
     * Regular expression pattern for validating numbers.
     */
    public static final String NUMBER_PATTERN = "^[0-9]+$";

    /**
     * Regular expression pattern for validating usernames, allowing alphanumeric
     * characters, dashes, and underscores.
     */
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]+$";

    /**
     * List of supported image file extensions.
     */
    public static final List<String> IMAGE_EXTENSION_LIST =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "raw", "psd", "ai", "eps");

    /**
     * List of media types that are visible.
     */
    public static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA);

    /**
     * List of sensitive HTTP headers.
     */
    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("authorization", "proxy-authorization");

    /**
     * For preventing Sonar issues related to sensitive headers.
     *
     * @return List of sensitive headers.
     */
    public static List<String> getSensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

    /**
     * Constants for common HTTP headers.
     */
    public interface HeaderType {
        String CONTENT_TYPE = "Content-Type";
        String X_API_KEY = "x-api-key";
    }

    /**
     * Security-related constants.
     */
    public static final class Security {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer";
        public static final String DEFAULT_REGISTRATION_ID = "oidc";
    }

    /**
     * Token-related properties.
     */
    public static final class TokenProperties {
        public static final String USERNAME = "preferred_username";
        public static final String ID = "sub";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String INDIVIDUAL_ID = "individual_id";
        public static final String ORGANIZATION_ID = "organization_id";
    }

    /**
     * Keycloak error codes.
     */
    public static final class KeyCloakError {
        public static final String INVALID_GRANT = "INVALID_GRANT";
        public static final String DISABLED = "DISABLED";
        public static final String INVALID = "INVALID";
    }

    /**
     * Constants related to XML.
     */
    public static final class XmlConst {
        public static final String TAG_OPEN_RETURN = "<return>";
        public static final String TAG_CLOSE_RETURN = "</return>";
        public static final String AND_LT_SEMICOLON = "&lt;";
        public static final String AND_GT_SEMICOLON = "&gt;";
        public static final String LT_CHARACTER = "<";
        public static final String GT_CHARACTER = ">";
    }

    /**
     * Logging related titles and sizes.
     */
    public static final class LoggingTitle {
        public static final String REQUEST = "\n-- REQUEST --\n";
        public static final String REQUEST_HEADER = "\n-- REQUEST HEADER --\n";
        public static final String REQUEST_PARAM = "-- REQUEST PARAM --\n";
        public static final String REQUEST_BODY = "-- REQUEST BODY --\n";
        public static final String RESPONSE = "\n-- RESPONSE --\n";
        public static final String PREFIX = "|>";
        public static final Integer BODY_SIZE_REQUEST_MAX = 1000;
        public static final Integer BODY_SIZE_RESPONSE_MAX = 1000;
    }

    /**
     * Sorting related constants.
     */
    public static final class Sorting {
        public static final String SPLIT_OPERATOR = ",";
        public static final String MINUS_OPERATOR = "-";
        public static final String PLUS_OPERATOR = "+";
        public static final String DESC = "desc";
        public static final String ASC = "asc";
        public static final String FILED_DISPLAY = "$1_$2";
    }

    public static final HashSet<String> EXCLUDE_LOGGING_ENDPOINTS = new HashSet<>(List.of("/actuator/health"));

    public static final int MAX_BYTE = 4096;

    public static class POOL {
        public static final String REST_CLIENT_POLL = "Rest-client-Pool"; // name of Rest client poll for https proxy
    }
}
