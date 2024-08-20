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
 * {@code CommonConstant} defines a collection of constant values used
 * throughout the application.
 * <p>
 * This class contains various constants including string literals, regular
 * expressions, and patterns used for common operations and validations. The
 * constants include commonly used symbols, patterns for validating phone
 * numbers, date formats, and other input data formats.
 * </p>
 */
public class CommonConstant {
    /**
     * Token for MSISDN (Mobile Station International Subscriber Directory Number)
     */
    public static final String MSISDN_TOKEN = "msisdn";

    /** Parameter name for 'name' */
    public static final String NAME_PARAM = "name";

    /** Parameter name for 'path' */
    public static final String PATH_PARAM = "path";

    /** Request ID parameter */
    public static final String REQUEST_ID = "requestId";

    /**
     * Date format with year, month, day, hour, minute, and second (yyyy-MM-dd
     * HH:mm:ss).
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date format with year, month, day, hour, minute, second, and millisecond
     * (yyyy-MM-dd HH:mm:ss.S).
     */
    public static final String DATE_FORMAT_MILI = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * Date format with hour, minute, second, day, month, and year (HH:mm:ss
     * dd-MM-yyyy).
     */
    public static final String DATE_FORMAT_2 = "HH:mm:ss dd-MM-yyyy";

    /**
     * Date format with hour, minute, second, day, month, and year (HH:mm:ss
     * dd/MM/yyyy).
     */
    public static final String DATE_FORMAT_3 = "HH:mm:ss dd/MM/yyyy";

    /**
     * Short date format with year, month, day, hour, minute, and second (y-M-d
     * H:m:s).
     */
    public static final String DATE_FORMAT_SHORT = "y-M-d H:m:s";

    /**
     * Short date format with year, month, and day (y-M-d).
     */
    public static final String DATE_FORMAT_SHORT_Y_M_D = "y-M-d";

    /**
     * Short date format with year and month (y-M-).
     */
    public static final String DATE_FORMAT_SHORT_Y_M = "y-M-";

    /**
     * Short date format with month (M).
     */
    public static final String DATE_FORMAT_SHORT_M = "M";

    /**
     * Short date format with year (y).
     */
    public static final String DATE_FORMAT_SHORT_Y = "y";

    /**
     * Short date format with year (yyyy).
     */
    public static final String DATE_FORMAT_SHORT_YYYY = "yyyy";

    /**
     * Short date format with month (MM).
     */
    public static final String DATE_FORMAT_SHORT_MM = "MM";

    /**
     * Short date format with day (dd).
     */
    public static final String DATE_FORMAT_SHORT_DD = "dd";

    /**
     * Date format with month and day without time (MM/dd/yyyy).
     */
    public static final String DATE_FORMAT_NO_TIME = "MM/dd/yyyy";

    /**
     * Date format with year, month, and day (yyyy-MM-dd).
     */
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    /**
     * Date format with year, month, and day with the day set to the first of the
     * month (yyyy-MM-01).
     */
    public static final String DATE_FORMAT_YM01 = "yyyy-MM-01";

    /**
     * Date format with day, month, and year (dd/MM/yyyy).
     */
    public static final String DATE_FORMAT_DMY = "dd/MM/yyyy";

    /**
     * Date format with month and year (MM/yyyy).
     */
    public static final String DATE_FORMAT_S_MY = "MM/yyyy";

    /**
     * Date format with year and month (yyyy/MM).
     */
    public static final String DATE_FORMAT_S_YM = "yyyy/MM";

    /**
     * Date format with year, month, and day (yyyy/MM/dd).
     */
    public static final String DATE_FORMAT_S_YMD = "yyyy/MM/dd";

    /**
     * Date format with year, month, day, hour, minute, and second (yyyy/MM/dd
     * HH:mm:ss).
     */
    public static final String DATE_FORMAT_S_YMD_HMS = "yyyy/MM/dd HH:mm:ss";

    /**
     * Date format with year, month, day, hour, minute, and second (yyyy-MM-dd
     * HH:mm:ss).
     */
    public static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date format with year, month, day, hour, minute, and second with a single
     * quote around the time (yyyy-MM-dd'T'HH:mm:ss).
     */
    public static final String DATE_FORMAT_YMD_T_HMS = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Date format with year, month, day, and fixed time at midnight
     * (yyyy-MM-dd'T'00:00:00).
     */
    public static final String DATE_FORMAT_YMD_T_HH_MM_SS = "yyyy-MM-dd'T'00:00:00";

    /**
     * Date format with day, month, year, hour, minute, and second (dd/MM/yyyy
     * HH:mm:ss).
     */
    public static final String DATE_FORMAT_DMY_HMS = "dd/MM/yyyy HH:mm:ss";

    /**
     * Date format with month, day, year in 12-hour clock (MM/dd/yyyy hh:mm:ss a).
     */
    public static final String DATE_FORMAT_MDY_HMS_12_HOUR = "MM/dd/yyyy hh:mm:ss a";

    /**
     * Date format with day, month, year, hour, minute, and second (dd-MM-yyyy
     * HH:mm:ss).
     */
    public static final String DATE_FORMAT_DMYHMS = "dd-MM-yyyy HH:mm:ss";

    /**
     * Date format with day, month, year, hour, and minute (dd-MM-yyyy HH:mm).
     */
    public static final String DATE_FORMAT_DMYHM = "dd-MM-yyyy HH:mm";

    /**
     * Date format with year, month, day, hour, minute, and second in compact form
     * (yyyyMMddHHmmss).
     */
    public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";

    /**
     * Date format with year, month, day, and hour in compact form (yyyyMMddHH).
     */
    public static final String DATE_FORMAT_YMDH = "yyyyMMddHH";

    /**
     * Date format with year and month in compact form (yyyyMM).
     */
    public static final String DATE_FORMAT_YM2 = "yyyyMM";

    /**
     * Date format with hour, minute, and second (HH:mm:ss).
     */
    public static final String DATE_FORMAT_HMS = "HH:mm:ss";

    /**
     * Date format with hour, minute, and second in compact form (HHmmss).
     */
    public static final String DATE_FORMAT_HMS_NORMAL = "HHmmss";

    /**
     * Date format with year, month, and day in compact form (yyyyMMdd).
     */
    public static final String DATE_FORMAT_YDM_INSTANT = "yyyyMMdd";

    /**
     * Short date format with year, month, and day (yMd).
     */
    public static final String DATE_FORMAT_YDM = "yMd";

    /**
     * Short date format with year and month (yM).
     */
    public static final String DATE_FORMAT_YD = "yM";

    /**
     * Short date format with year and month separated by a hyphen (y-M).
     */
    public static final String DATE_FORMAT_Y_D = "y-M";

    /**
     * Short date format with year (y).
     */
    public static final String DATE_FORMAT_Y = "y";

    /**
     * Time format with hour and minute (hh:mm).
     */
    public static final String DATE_FORMAT_HH_MM = "hh:mm";

    /**
     * Date format with month, day, and hour, minute, and second (MM dd HH:mm:ss).
     */
    public static final String DATE_FORMAT_MDHMS = "MM dd HH:mm:ss";

    /**
     * Date and time format with hour, minute, and day, month, and year (HH:mm -
     * dd/MM/yyyy).
     */
    public static final String DATE_TIME_FORMAT_HMDMY = "HH:mm - dd/MM/yyyy";

    /**
     * Date format for end of the day (yyyy-MM-dd 23:59:59).
     */
    public static final String DATE_FORMAT_END_DAY = "yyyy-MM-dd 23:59:59";

    /**
     * Date format for beginning of the day (yyyy-MM-dd 00:00:00).
     */
    public static final String DATE_FORMAT_BEGIN_DAY = "yyyy-MM-dd 00:00:00";

    /**
     * Date format with year, month, day, and time in GMT+0
     * (yyyy-mm-dd'T'hh:mm:ss.000+0000).
     */
    public static final String DATE_FORMAT_YMDTHMS_ZER0 = "yyyy-mm-dd'T'hh:mm:ss.000+0000";

    /**
     * Date format with year, month, day, and time in GMT+0
     * (yyyy-MM-dd'T'HH:mm:ss.000+0000).
     */
    public static final String DATE_FORMAT_YMDTHMS_ZERO_24HRS = "yyyy-MM-dd'T'HH:mm:ss.000+0000";

    /**
     * Time format with hour, minute, and date (HH:mm | dd/MM/yyyy).
     */
    public static final String DATE_FORMAT_HM_DMY = "HH:mm | dd/MM/yyyy";

    /**
     * Time format with hour, minute, and date (HH:mm dd/MM/yyyy).
     */
    public static final String DATE_FORMAT_HM_DMY1 = "HH:mm dd/MM/yyyy";

    /**
     * Date format with year, month, day, and time in GMT+7
     * (yyyy-MM-dd'T'00:00:00+07:00).
     */
    public static final String DATE_FORMAT_YMDTHMS_GMT_7 = "yyyy-MM-dd'T'00:00:00+07:00";

    /**
     * Date format with year, month, day, and time in GMT+7
     * (yyyy-MM-dd'T'HH:mm:ss+07:00).
     */
    public static final String DATE_FORMAT_YMDTHMS_GMT_7_2 = "yyyy-MM-dd'T'HH:mm:ss+07:00";

    /**
     * Time format with hour and minute (HH:mm).
     */
    public static final String DATE_FORMAT_HM = "HH:mm";

    /**
     * Date format with day, month, year, and time in hour and minute
     * (dd/MM/yyyy-HH:mm).
     */
    public static final String DATE_FORMAT_DMY_HM = "dd/MM/yyyy-HH:mm";

    /**
     * Day start of the month format (01-).
     */
    public static final String DAY_START_MONTH = "01-";

    /**
     * Number of milliseconds in a year (31536000000L = 1000 * 60 * 60 * 24 * 365).
     */
    public static final Long MILI_SECOND_OF_YEAR = 31536000000L;

    /**
     * Number of milliseconds in an hour (3600000L = 1000 * 60 * 60).
     */
    public static final Long MILI_SECOND_OF_HOUR = 3600000L;

    /**
     * String of digits used for generating random salt values.
     */
    public static final String SALTCHARS = "1234567890";

    /**
     * Default timezone used (UTC).
     */
    public static final String DATE_TIMEZONE = "UTC";

    /**
     * Timezone offset for GMT+7 (GMT+07).
     */
    public static final String TIMEZONE_GMT7 = "GMT+07";

    /**
     * Date format with day, month, and year separated by hyphens (dd-MM-yyyy).
     */
    public static final String FORMAT_DATE_DMY_HYPHEN = "dd-MM-yyyy";

    /**
     * Short date format with day, month, and year separated by hyphens (d-M-y).
     */
    public static final String FORMAT_DATE_DMY_HYPHEN_SHORT = "d-M-y";

    /** Username parameter */
    public static final String USERNAME = "username";

    /** Password parameter */
    public static final String PASSWORD = "password";

    /** Key for OTP (One-Time Password) */
    public static final String OTP_KEY = "[otp]";

    /** String constant for carriage return */
    public static final String SLASH_R = "\r";

    /** String constant for newline */
    public static final String SLASH_N = "\n";

    /** String constant for double quotation mark */
    public static final String QUATATION = "\"";

    /** String constant for asterisk */
    public static final String ASTERISK = "*";

    /** String constant for comma */
    public static final String COMMA = ",";

    /** String constant for dot */
    public static final String DOT = ".";

    /** Character constant for comma */
    public static final char CHAR_COMMA = ',';

    /** Character constant for dot */
    public static final char CHAR_DOT = '.';

    /** String constant for semicolon */
    public static final String SEMICOLON = ";";

    /** String constant for strikethrough */
    public static final String STRIKETHROUGH = "-";

    /** String constant for vertical bar */
    public static final String VERTICAL = "\\|";

    /** Common declaration value of '1' */
    public static final String COMMON_DECL_VALUE_1 = "1";

    /** Regular expression for plus sign */
    public static final String REGEX_PLUS = "\\+";

    /**
     * Regular expression pattern for date in the format DD-MM-YYYY.
     */
    public static final String REGEX_DATE_DMY = "([0-9]{2})-([0-9]{2})-([0-9]{4})";

    /**
     * Regular expression pattern for Viettel phone number formats. Matches phone
     * numbers starting with Viettel's prefixes.
     */
    public static final String REGEX_VIETTEL_NUMBER_FORMAT =
            "^8496\\d{7}$|^8497\\d{7}$|^8498\\d{7}$|^8416\\d{8}$|0?96\\d{7}$|0?97\\d{7}$|^0?98\\d{7}$|^0?16\\d{8}$";

    /**
     * Regular expression pattern for paper number format with escape characters.
     */
    public static final String REGEX_PAPER_NUMBER_FORMAT = "/\\=(.*?)\\;/";

    /**
     * Regular expression pattern for paper number format (8 to 12 alphanumeric
     * characters).
     */
    public static final String REGEX_PAPER_NUMBER_FORMAT_2 = "^[0-9a-zA-Z]{8,12}$";

    /**
     * Regular expression pattern for date in the format DD-MM-YYYY with strict
     * validation.
     */
    public static final String REGEX_DATE_FORMAT_DMY =
            "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-((20|2[0-9])[0-9]{2})$";

    /**
     * Regular expression pattern for checking ID number with 9 to 12 characters.
     */
    public static final String REGEX_CHECK_ID_NO = "/^.{9,12}$/u";

    /**
     * Regular expression pattern for validating Base64 encoded strings.
     */
    public static final String REGEX_CHECK_BASE_64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";

    /**
     * Regular expression pattern for time in the format HH:MM:SS.
     */
    public static final String REGEX_CALENDAR_FROM_TIME = "(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])";

    /**
     * Regular expression pattern for checking that a string does not contain an
     * asterisk.
     */
    public static final String PATTERN_CHECK = "^[^*]+$";

    // Pattern constants
    /**
     * Regular expression pattern for validating phone numbers with 9 to 11 digits.
     */
    public static final String PHONE_PATTERN = "^[0-9]{9,11}$";

    /**
     * Regular expression pattern for validating phone numbers with 9 to 12 digits.
     */
    public static final String PHONE_PATTERN_2 = "^[0-9]{9,12}$";

    /**
     * Regular expression pattern for validating phone numbers that can include
     * asterisks and dashes.
     */
    public static final String REGEX_PHONE_ASTERISK = "^[-*0-9]+$";

    /**
     * Regular expression pattern for validating Base64 encoded strings (with a
     * potential trailing `=`).
     */
    public static final String REGEX_BASE_64 =
            "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

    /**
     * Regular expression pattern to ensure that a string does not contain
     * asterisks.
     */
    public static final String REGEX_ASTERISK = "^[^*]+$";

    /**
     * Regular expression pattern to match strings starting with a digit.
     */
    public static final String PREFIX_NUMBER_REGEX = "^[0-9].*$";

    /**
     * Regular expression pattern to filter out only numeric digits.
     */
    public static final String FILTER_NUMBER_REGEX = "\\d+";

    /**
     * Regular expression pattern to match newline characters (escaped).
     */
    public static final String REGEX_ENTER = "\\\\n";

    /**
     * Regular expression pattern to match newline characters.
     */
    public static final String REGEX_ENTER_2 = "\n";

    /**
     * Regular expression pattern to match whitespace characters, newlines, and
     * carriage returns in JSON.
     */
    public static final String REGEX_SPACE_JSON = "\\\\n|\\\\r|\\\\|\\s";

    /**
     * Regular expression pattern to match carriage returns in JSON.
     */
    public static final String REGEX_SPACE_JSON_2 = "\r";

    /**
     * Regular expression pattern to match vertical bars.
     */
    public static final String REGEX_VERTICAL_BRICK = "\\|";

    /**
     * Regular expression pattern to match commas.
     */
    public static final String REGEX_COMMAS = ",";

    /**
     * Regular expression pattern to match underscores.
     */
    public static final String REGEX_UNDERLINE = "_";

    /**
     * Regular expression pattern to match dashes.
     */
    public static final String REGEX_DASH = "-";

    /**
     * Regular expression pattern to match diagonal dashes.
     */
    public static final String REGEX_DIAGONAL = "-";

    /**
     * Regular expression pattern to match double diagonal dashes.
     */
    public static final String REGEX_DIAGONAL2 = "- -";

    /**
     * Regular expression pattern to match colons.
     */
    public static final String REGEX_COLON = ":";

    /**
     * Regular expression pattern to match dots.
     */
    public static final String REGEX_DOT = ".";

    /**
     * Regular expression pattern to match '@' symbols.
     */
    public static final String REGEX_AT = "@";

    /**
     * Regular expression pattern to split strings by dots.
     */
    public static final String REGEX_SPLIT_DOT = "\\.";

    /**
     * Regular expression pattern to check pack codes (alphanumeric and
     * underscores).
     */
    public static final String REGEX_CHECK_PACK_CODE = "^[a-zA-Z0-9_]+$";

    /**
     * Regular expression pattern to validate MAC addresses (hexadecimal digits and
     * colons).
     */
    public static final String REGEX_CHECK_MAC_ADDRESS = "^[a-zA-Z0-9:]+$";

    /**
     * Regular expression pattern for service type in sub-IDs, separated by commas.
     */
    public static final String REGEX_PREG_SUBIDS_SERVICE_TYPE = "[^;]*,";

    /**
     * Regular expression pattern to strip HTML tags from strings.
     */
    public static final String REGEX_STRIP_HTML_TAG = "<[^>]*>";

    /**
     * Regular expression pattern to match vertical bars (alternative pattern).
     */
    public static final String REGEX_VERTICAL_BRICK_2 = "\\|";

    /**
     * String literal for the HTML entity "amp;".
     */
    public static final String AMP = "amp;";

    /**
     * {@code COMMON_PREFIX} contains commonly used regular expression patterns
     * related to number validation.
     */
    public interface COMMON_PREFIX {
        /**
         * Regular expression pattern to match numbers with optional leading dashes or
         * asterisks.
         */
        public static final String NUMBER_PREFIX = "^[-*0-9]+$";

        /**
         * Regular expression pattern to match characters that are not digits or dots.
         */
        public static final String REGEX_ONLY_NUMBER = "[^\\d.]";

        /**
         * Regular expression pattern to match characters that are not digits.
         */
        public static final String REGEX_NOT_NUMBER = "[^0-9]+";
    }
}
