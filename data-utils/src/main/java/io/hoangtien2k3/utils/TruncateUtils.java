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
package io.hoangtien2k3.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Utility class for truncating strings and handling serialization of objects.
 * <p>
 * The {@code TruncateUtils} class provides methods for truncating a string to
 * ensure it does not exceed a specified byte length when encoded in UTF-8. It
 * also includes methods for serializing objects and handling form data
 * truncation.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class contains methods to truncate strings based on byte length,
 * serialize objects to strings, and handle form data truncation.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>truncate</strong>: Truncates a string to fit within a specified
 * byte length when encoded in UTF-8.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li>{@code s} ({@code String}): The string to be truncated. Can be
 * {@code null} or empty.</li>
 * <li>{@code maxByte} ({@code int}): The maximum byte length for the truncated
 * string.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A truncated version of the input string if it exceeds the specified byte
 * length, otherwise the original string.
 * </p>
 * </li>
 * <li><strong>Exceptions:</strong>
 * <p>
 * Logs an error if an exception occurs during the truncation process. Returns
 * the original string if an error occurs.
 * </p>
 * </li>
 * </ul>
 * </li>
 * <li><strong>truncateBody</strong>: Truncates a string to fit within a
 * specified byte length when encoded in UTF-8.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li>{@code s} ({@code String}): The string to be truncated.</li>
 * <li>{@code maxByte} ({@code int}): The maximum byte length for the truncated
 * string.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A truncated version of the input string if it exceeds the specified byte
 * length, otherwise the original string.
 * </p>
 * </li>
 * <li><strong>Notes:</strong>
 * <p>
 * This method calculates the number of bytes required for each character in the
 * string to ensure that the truncation does not break multi-byte characters.
 * </p>
 * </li>
 * </ul>
 * </li>
 * <li><strong>truncateBody(Object responseBody)</strong>: Serializes an object
 * to a JSON string and truncates it.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li>{@code responseBody} ({@code Object}): The object to be serialized and
 * truncated.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A JSON representation of the object, truncated if necessary. If serialization
 * fails, returns a placeholder string.
 * </p>
 * </li>
 * <li><strong>Exceptions:</strong>
 * <p>
 * Logs an error if an exception occurs during serialization. Returns a
 * placeholder string if an error occurs.
 * </p>
 * </li>
 * </ul>
 * </li>
 * <li><strong>truncateBody(MultiValueMap<String, String> formData)</strong>:
 * Truncates and concatenates form data.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li>{@code formData} ({@code MultiValueMap<String, String>}): The form data
 * to be truncated and concatenated.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A concatenated string representation of the form data, with values truncated
 * as necessary.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * String longString = "A very long string that needs to be truncated...";
 * String truncatedString = TruncateUtils.truncate(longString, 50);
 * // truncatedString will contain the first 50 bytes of the original string
 *
 * Object responseObject = new SomeObject();
 * String jsonString = TruncateUtils.truncateBody(responseObject);
 * // jsonString will contain the JSON representation of responseObject
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * The UTF-8 byte length calculation considers multi-byte characters to ensure
 * that truncation does not cut characters in the middle. The class uses
 * {@code ObjectMapper} for serialization of objects and handles exceptions
 * gracefully by logging errors and returning default values when necessary.
 * </p>
 */
@Slf4j
public class TruncateUtils {

    /**
     * Truncates a string to fit within a specified byte length when encoded in
     * UTF-8.
     * <p>
     * This method first checks if the string is null or empty, and if so, returns
     * it as is. If the string is longer than the specified byte length, it is
     * truncated while preserving multi-byte characters.
     * </p>
     *
     * @param s
     *            The string to be truncated. Can be <code>null</code> or empty.
     * @param maxByte
     *            The maximum byte length for the truncated string.
     * @return A truncated version of the input string if it exceeds the specified
     *         byte length, otherwise the original string.
     */
    public static String truncate(String s, int maxByte) {
        try {
            if (DataUtil.isNullOrEmpty(s)) {
                return s;
            }
            final byte[] utf8Bytes = s.getBytes(StandardCharsets.UTF_8);
            if (utf8Bytes.length <= maxByte) {
                return s;
            }
            return truncateBody(s, maxByte);
        } catch (Exception ex) {
            log.error("Can't truncate ", ex);
        }
        return s;
    }

    /**
     * Truncates a string to fit within a specified byte length when encoded in
     * UTF-8.
     * <p>
     * This method iterates over the characters in the string, calculating the byte
     * length for each character and truncating the string when the total byte
     * length exceeds the specified limit.
     * </p>
     *
     * @param s
     *            The string to be truncated.
     * @param maxByte
     *            The maximum byte length for the truncated string.
     * @return A truncated version of the input string if it exceeds the specified
     *         byte length, otherwise the original string.
     */
    public static String truncateBody(String s, int maxByte) {
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // ranges from http://en.wikipedia.org/wiki/UTF-8
            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            } else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                // surrogate area, consume next char as well
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }

            if (b + more > maxByte) {
                return s.substring(0, i);
            }
            b += more;
            i += skip;
        }
        return s;
    }

    /**
     * Serializes an object to a JSON string and truncates it to fit within a
     * specified byte length.
     * <p>
     * This method uses Jackson's `ObjectMapper` to convert the object to a JSON
     * string and then truncates it. If serialization fails, a placeholder string is
     * returned.
     * </p>
     *
     * @param responseBody
     *            The object to be serialized and truncated.
     * @return A JSON representation of the object, truncated if necessary. Returns
     *         a placeholder string if serialization fails.
     */
    public static String truncateBody(Object responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            log.error("Exception when parse response to string, ignore response", e);
            return "Truncated and remove if has exception";
        }
    }

    /**
     * Truncates and concatenates form data.
     * <p>
     * This method iterates over the entries in a {@link MultiValueMap}, truncates
     * the values, and concatenates them into a single string representation.
     * </p>
     *
     * @param formData
     *            The form data to be truncated and concatenated.
     */
    private String truncateBody(MultiValueMap<String, String> formData) {
        StringBuilder messageResponse = new StringBuilder();
        Set<String> keys = formData.keySet();
        for (String key : keys) {
            messageResponse.append(key).append(":").append(truncateBody(formData.get(key)));
        }
        return messageResponse.toString();
    }
}
