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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Utility class for handling internationalized messages.
 * <p>
 * The {@code MessageUtils} class provides methods to retrieve localized
 * messages from a resource bundle. It supports message formatting and fallback
 * mechanisms for missing or erroneous messages.
 * </p>
 * <h2>Class Overview:</h2>
 * <p>
 * The class uses {@code ResourceBundle} to fetch messages based on the provided
 * locale. It formats the message using {@code MessageFormat} and handles
 * exceptions to ensure that the application continues to function even if a
 * message is missing or the resource bundle is misconfigured.
 * </p>
 *
 * <h2>Fields:</h2>
 * <ul>
 * <li><strong>BASE_NAME</strong>: The base name of the resource bundle, which
 * is typically the name of the properties file (without the .properties
 * extension) containing the messages. Defaults to "messages".</li>
 * </ul>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>getMessage(String code, Locale locale)</strong>: Retrieves the
 * message associated with the given code and locale. If the message is missing
 * or an error occurs, the code itself is returned as a fallback.
 * <p>
 * <b>Parameters:</b>
 * </p>
 * <ul>
 * <li><strong>code</strong>: The key for the desired message.</li>
 * <li><strong>locale</strong>: The locale to use for message retrieval.</li>
 * </ul>
 * <p>
 * <b>Returns:</b>
 * </p>
 * <ul>
 * <li>The localized message, or the code itself if the message is not
 * found.</li>
 * </ul>
 * </li>
 *
 * <li><strong>getMessage(String code, Locale locale, Object... args)</strong>:
 * Retrieves and formats the message associated with the given code and locale,
 * using the provided arguments.
 * <p>
 * <b>Parameters:</b>
 * </p>
 * <ul>
 * <li><strong>code</strong>: The key for the desired message.</li>
 * <li><strong>locale</strong>: The locale to use for message retrieval.</li>
 * <li><strong>args</strong>: Arguments to format the message.</li>
 * </ul>
 * <p>
 * <b>Returns:</b>
 * </p>
 * <ul>
 * <li>The formatted message, or the code itself if the message is not
 * found.</li>
 * </ul>
 * </li>
 *
 * <li><strong>getMessage(String code)</strong>: Retrieves the message
 * associated with the given code, using the default locale from
 * {@code LocaleContextHolder}.
 * <p>
 * <b>Parameters:</b>
 * </p>
 * <ul>
 * <li><strong>code</strong>: The key for the desired message.</li>
 * </ul>
 * <p>
 * <b>Returns:</b>
 * </p>
 * <ul>
 * <li>The localized message, or the code itself if the message is not
 * found.</li>
 * </ul>
 * </li>
 *
 * <li><strong>getMessage(String code, Object... args)</strong>: Retrieves and
 * formats the message associated with the given code, using the default locale
 * from {@code LocaleContextHolder} and the provided arguments.
 * <p>
 * <b>Parameters:</b>
 * </p>
 * <ul>
 * <li><strong>code</strong>: The key for the desired message.</li>
 * <li><strong>args</strong>: Arguments to format the message.</li>
 * </ul>
 * <p>
 * <b>Returns:</b>
 * </p>
 * <ul>
 * <li>The formatted message, or the code itself if the message is not
 * found.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * // Assuming there is a message property file named "messages.properties" with
 * // a key "welcome.message"
 * String message = MessageUtils.getMessage("welcome.message", Locale.US);
 * // Output: "Welcome to our application!"
 *
 * // With arguments
 * String formattedMessage = MessageUtils.getMessage("welcome.user", Locale.US, "John");
 * // Output: "Welcome to our application, John!"
 *
 * // Using default locale
 * String defaultMessage = MessageUtils.getMessage("default.message");
 * // Output will be based on the default locale set in LocaleContextHolder
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * The `MessageUtils` class relies on `ResourceBundle` for message retrieval.
 * Ensure that the resource bundle files (e.g., `messages.properties`,
 * `messages_en.properties`) are correctly placed in the classpath. The class
 * also uses `LocaleContextHolder` to fetch the default locale, which should be
 * properly configured in your Spring application context.
 * </p>
 *
 * <p>
 * Logging is performed for exceptions occurring during message retrieval,
 * helping in debugging issues with missing or malformed messages.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class MessageUtils {

    private static final String BASE_NAME = "messages";

    /**
     * Retrieves the message associated with the given code and locale.
     *
     * @param code
     *            The key for the desired message.
     * @param locale
     *            The locale to use for message retrieval.
     * @return The localized message, or the code itself if the message is not
     *         found.
     */
    public static String getMessage(String code, Locale locale) {
        return getMessage(code, locale, null);
    }

    /**
     * Retrieves and formats the message associated with the given code and locale,
     * using the provided arguments.
     *
     * @param code
     *            The key for the desired message.
     * @param locale
     *            The locale to use for message retrieval.
     * @param args
     *            Arguments to format the message.
     * @return The formatted message, or the code itself if the message is not
     *         found.
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
     * Retrieves the message associated with the given code, using the default
     * locale from LocaleContextHolder.
     *
     * @param code
     *            The key for the desired message.
     * @return The localized message, or the code itself if the message is not
     *         found.
     */
    public static String getMessage(String code) {
        return getMessage(code, LocaleContextHolder.getLocale(), null);
    }

    /**
     * Retrieves and formats the message associated with the given code, using the
     * default locale from LocaleContextHolder and the provided arguments.
     *
     * @param code
     *            The key for the desired message.
     * @param args
     *            Arguments to format the message.
     * @return The formatted message, or the code itself if the message is not
     *         found.
     */
    public static String getMessage(String code, Object... args) {
        return getMessage(code, LocaleContextHolder.getLocale(), args);
    }
}
