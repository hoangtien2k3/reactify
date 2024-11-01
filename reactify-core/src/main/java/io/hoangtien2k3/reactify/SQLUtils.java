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
package io.hoangtien2k3.reactify;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * SQLUtils class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class SQLUtils {
    /**
     * <p>
     * replaceSpecialDigit.
     * </p>
     *
     * @param value
     *            a {@link String} object
     * @return a {@link String} object
     */
    public static String replaceSpecialDigit(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replace("%", "\\%").replace("_", "\\_");
            return value;
        }
        return "";
    }
}
