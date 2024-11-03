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
package com.reactify.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
@Slf4j
public class MessageUtils {

    private static final String BASE_NAME = "messages";

    /**
     * Constructs a new instance of {@code MessageUtils}.
     */
    public MessageUtils() {}

    /**
     * <p>
     * getMessage.
     * </p>
     *
     * @param code
     *            a {@link String} object
     * @param locale
     *            a {@link Locale} object
     * @return a {@link String} object
     */
    public static String getMessage(String code, Locale locale) {
        return getMessage(code, locale, (Object) null);
    }

    /**
     * <p>
     * getMessage.
     * </p>
     *
     * @param code
     *            a {@link String} object
     * @param locale
     *            a {@link Locale} object
     * @param args
     *            a {@link Object} object
     * @return a {@link String} object
     */
    public static String getMessage(String code, Locale locale, Object... args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(BASE_NAME, locale);
        String message;
        try {
            message = resourceBundle.getString(code);
            message = MessageFormat.format(message, args);
        } catch (Exception ex) {
            log.debug(ex.getMessage(), ex);
            message = code;
        }

        return message;
    }

    /**
     * <p>
     * getMessage.
     * </p>
     *
     * @param code
     *            a {@link String} object
     * @return a {@link String} object
     */
    public static String getMessage(String code) {
        return getMessage(code, LocaleContextHolder.getLocale(), (Object) null);
    }

    /**
     * <p>
     * getMessage.
     * </p>
     *
     * @param code
     *            a {@link String} object
     * @param args
     *            a {@link Object} object
     * @return a {@link String} object
     */
    public static String getMessage(String code, Object... args) {
        return getMessage(code, LocaleContextHolder.getLocale(), args);
    }
}
