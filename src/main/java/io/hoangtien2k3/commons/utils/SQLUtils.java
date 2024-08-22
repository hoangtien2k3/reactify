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
package io.hoangtien2k3.commons.utils;

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
