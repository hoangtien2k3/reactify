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

public class CommonConstant {
  public static final String MSISDN_TOKEN = "msisdn";
  public static final String NAME_PARAM = "name";
  public static final String PATH_PARAM = "path";
  public static final String REQUEST_ID = "requestId";

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_MILI = "yyyy-MM-dd HH:mm:ss.S";
  public static final String DATE_FORMAT_2 = "HH:mm:ss dd-MM-yyyy";
  public static final String DATE_FORMAT_3 = "HH:mm:ss dd/MM/yyyy";
  public static final String DATE_FORMAT_SHORT = "y-M-d H:m:s";
  public static final String DATE_FORMAT_SHORT_Y_M_D = "y-M-d";
  public static final String DATE_FORMAT_SHORT_Y_M = "y-M-";
  public static final String DATE_FORMAT_SHORT_M = "M";
  public static final String DATE_FORMAT_SHORT_Y = "y";
  public static final String DATE_FORMAT_SHORT_YYYY = "yyyy";
  public static final String DATE_FORMAT_SHORT_MM = "MM";
  public static final String DATE_FORMAT_SHORT_DD = "dd";
  public static final String DATE_FORMAT_NO_TIME = "MM/dd/yyyy";
  public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
  public static final String DATE_FORMAT_YM01 = "yyyy-MM-01";
  public static final String DATE_FORMAT_DMY = "dd/MM/yyyy";
  public static final String DATE_FORMAT_S_MY = "MM/yyyy";
  public static final String DATE_FORMAT_S_YM = "yyyy/MM";
  public static final String DATE_FORMAT_S_YMD = "yyyy/MM/dd";
  public static final String DATE_FORMAT_S_YMD_HMS = "yyyy/MM/dd HH:mm:ss";
  public static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_YMD_T_HMS = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String DATE_FORMAT_YMD_T_HH_MM_SS = "yyyy-MM-dd'T'00:00:00";
  public static final String DATE_FORMAT_DMY_HMS = "dd/MM/yyyy HH:mm:ss";
  public static final String DATE_FORMAT_MDY_HMS_12_HOUR = "MM/dd/yyyy hh:mm:ss a";
  public static final String DATE_FORMAT_DMYHMS = "dd-MM-yyyy HH:mm:ss";
  public static final String DATE_FORMAT_DMYHM = "dd-MM-yyyy HH:mm";
  public static final String DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss";
  public static final String DATE_FORMAT_YMDH = "yyyyMMddHH";
  public static final String DATE_FORMAT_YM2 = "yyyyMM";
  public static final String DATE_FORMAT_HMS = "HH:mm:ss";
  public static final String DATE_FORMAT_HMS_NORMAL = "HHmmss";
  public static final String DATE_FORMAT_YDM_INSTANT = "yyyyMMdd";
  public static final String DATE_FORMAT_YDM = "yMd";
  public static final String DATE_FORMAT_YD = "yM";
  public static final String DATE_FORMAT_Y_D = "y-M";
  public static final String DATE_FORMAT_Y = "y";
  public static final String DATE_FORMAT_HH_MM = "hh:mm";
  public static final String DATE_FORMAT_MDHMS = "MM dd HH:mm:ss";
  public static final String DATE_TIME_FORMAT_HMDMY = "HH:mm - dd/MM/yyyy";
  public static final String DATE_FORMAT_END_DAY = "yyyy-MM-dd 23:59:59";
  public static final String DATE_FORMAT_BEGIN_DAY = "yyyy-MM-dd 00:00:00";
  public static final String DATE_FORMAT_YMDTHMS_ZER0 = "yyyy-mm-dd'T'hh:mm:ss.000+0000";
  public static final String DATE_FORMAT_YMDTHMS_ZERO_24HRS = "yyyy-MM-dd'T'HH:mm:ss.000+0000";
  public static final String DATE_FORMAT_HM_DMY = "HH:mm | dd/MM/yyyy";
  public static final String DATE_FORMAT_HM_DMY1 = "HH:mm dd/MM/yyyy";
  public static final String DATE_FORMAT_YMDTHMS_GMT_7 = "yyyy-MM-dd'T'00:00:00+07:00";
  public static final String DATE_FORMAT_YMDTHMS_GMT_7_2 = "yyyy-MM-dd'T'HH:mm:ss+07:00";
  public static final String DATE_FORMAT_HM = "HH:mm";
  public static final String DATE_FORMAT_DMY_HM = "dd/MM/yyyy-HH:mm";
  public static final String DAY_START_MONTH = "01-";
  public static final Long MILI_SECOND_OF_YEAR = 31536000000L; // 1000*60*60*24*365L=31536000000L
  public static final Long MILI_SECOND_OF_HOUR = 3600000L; // 1000*60*60=36000000L
  public static final String SALTCHARS = "1234567890";
  public static final String DATE_TIMEZONE = "UTC";
  public static final String TIMEZONE_GMT7 = "GMT+07";

  public static final String FORMAT_DATE_DMY_HYPHEN = "dd-MM-yyyy";
  public static final String FORMAT_DATE_DMY_HYPHEN_SHORT = "d-M-y";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String OTP_KEY = "[otp]";

  public static final String SLASH_R = "\r";
  public static final String SLASH_N = "\n";
  public static final String QUATATION = "\"";
  public static final String ASTERISK = "*";
  public static final String COMMA = ",";
  public static final String DOT = ".";
  public static final char CHAR_COMMA = ',';
  public static final char CHAR_DOT = '.';
  public static final String SEMICOLON = ";";
  public static final String STRIKETHROUGH = "-";
  public static final String VERTICAL = "\\|";
  public static final String COMMON_DECL_VALUE_1 = "1";
  public static final String REGEX_PLUS = "\\+";
  public static final String REGEX_DATE_DMY = "([0-9]{2})-([0-9]{2})-([0-9]{4})";
  public static final String REGEX_VIETTEL_NUMBER_FORMAT = "^8496\\d{7}$|^8497\\d{7}$|^8498\\d{7}$|^8416\\d{8}$|0?96\\d{7}$|0?97\\d{7}$|^0?98\\d{7}$|^0?16\\d{8}$";
  public static final String REGEX_PAPER_NUMBER_FORMAT = "/\\=(.*?)\\;/";
  public static final String REGEX_PAPER_NUMBER_FORMAT_2 = "^[0-9a-zA-Z]{8,12}$";
  public static final String REGEX_DATE_FORMAT_DMY = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-((20|2[0-9])[0-9]{2})$";
  public static final String REGEX_CHECK_ID_NO = "/^.{9,12}$/u";
  public static final String REGEX_CHECK_BASE_64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
  public static final String REGEX_CALENDAR_FROM_TIME = "(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])";
  public static final String PATTER_CHECK = "^[^*]+$";

  // pattern
  public static final String PHONE_PATTERN = "^[0-9]{9,11}$";
  public static final String PHONE_PATTERN_2 = "^[0-9]{9,12}$";
  public static final String REGEX_PHONE_ASTERISK = "^[-*0-9]+$";
  public static final String REGEX_BASE_64 = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
  public static final String REGEX_ASTERISK = "^[^*]+$";
  public static final String PREFIX_NUMBER_REGEX = "^[0-9].*$";
  public static final String FILTER_NUMBER_REGEX = "\\d+";
  public static final String REGEX_ENTER = "\\\\n";
  public static final String REGEX_ENTER_2 = "\n";
  public static final String REGEX_SPACE_JSON = "\\\\n|\\\\r|\\\\|\\s";
  public static final String REGEX_SPACE_JSON_2 = "\r";
  public static final String REGEX_VERTICAL_BRICK = "|";
  public static final String REGEX_COMMAS = ",";
  public static final String REGEX_UNDERLINE = "_";
  public static final String REGEX_DASH = "-";
  public static final String REGEX_DIAGONAL = "-";
  public static final String REGEX_DIAGONAL2 = "- -";
  public static final String REGEX_COLON = ":";
  public static final String REGEX_DOT = ".";
  public static final String REGEX_AT = "@";
  public static final String REGEX_SPLIT_DOT = "\\.";
  public static final String REGEX_CHECK_PACK_CODE = "^[a-zA-Z0-9_]+$";
  public static final String REGEX_CHECK_MAC_ADDRESS = "^[a-zA-Z0-9:]+$";
  public static final String REGEX_PREG_SUBIDS_SERVICE_TYPE = "[^;]*,";
  public static final String REGEX_STRIP_HTML_TAG = "<[^>]*>";
  public static final String REGEX_VERTICAL_BRICK_2 = "\\|";
  public static final String AMP = "amp;";

  public interface COMMON_PREFIX {
    public static final String NUMBER_PREFIX = "^[-*0-9]+$";
    public static final String REGEX_ONLY_NUMBER = "[^\\d.]";
    public static final String REGEX_NOT_NUMBER = "[^0-9]+";
  }
}
