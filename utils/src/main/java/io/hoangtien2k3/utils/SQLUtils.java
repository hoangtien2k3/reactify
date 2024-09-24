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

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for handling SQL-related operations.
 * <p>
 * The {@code SQLUtils} class provides a static method to replace special
 * characters in a string to make it suitable for use in SQL queries. This is
 * particularly useful for escaping characters that have special meanings in SQL
 * like `%` and `_`, which are used in wildcard patterns.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class contains utility methods for SQL operations. Specifically, it
 * provides a method to replace special characters in a string that can
 * interfere with SQL query syntax.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>replaceSpecialDigit</strong>: Replaces special characters in a
 * string to prevent them from being interpreted as SQL wildcards.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>value</code> (<code>String</code>): The string to be processed. It
 * can be any value that might contain special characters.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * The processed string with special characters replaced. Specifically, the
 * percent sign (`%`) and underscore (`_`) are replaced with their escaped
 * versions (`\%` and `\_` respectively). If the input string is empty or
 * <code>null</code>, an empty string is returned.
 * </p>
 * </li>
 * <li><strong>Usage:</strong>
 * <p>
 * This method is used to escape special characters in SQL queries to prevent
 * them from being interpreted as wildcards. For example, if you need to include
 * a literal percent sign in your SQL query, you would use this method to
 * replace `%` with `\%`.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * String rawValue = "100%";
 * String safeValue = SQLUtils.replaceSpecialDigit(rawValue);
 * // safeValue will be "100\\%"
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * This utility method is designed to handle basic escaping of special
 * characters for SQL queries. Depending on the SQL database being used,
 * additional escaping or handling might be required for other special
 * characters or SQL injection prevention.
 * </p>
 *
 * @author hoangtien2k3
 */
public class SQLUtils {

    /**
     * Replaces special characters in a string to prevent them from being
     * interpreted as SQL wildcards.
     * <p>
     * Specifically, the percent sign (`%`) and underscore (`_`) are replaced with
     * their escaped versions (`\%` and `\_` respectively). This is useful for
     * ensuring that these characters are treated as literals in SQL queries rather
     * than wildcards.
     * </p>
     *
     * @param value
     *            The string to be processed. Can be <code>null</code> or empty.
     * @return The processed string with special characters replaced. Returns an
     *         empty string if the input is <code>null</code> or empty.
     */
    public static String replaceSpecialDigit(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replace("%", "\\%").replace("_", "\\_");
            return value;
        }
        return "";
    }
}
