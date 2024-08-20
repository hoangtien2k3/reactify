package io.hoangtien2k3.commons.utils;

import org.apache.commons.lang3.StringUtils;

public class SQLUtils {
    public static String replaceSpecialDigit(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replace("%", "\\%").replace("_", "\\_");
            return value;
        }
        return "";
    }
}
