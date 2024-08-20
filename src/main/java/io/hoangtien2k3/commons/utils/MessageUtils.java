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

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class MessageUtils {

  private static final String BASE_NAME = "messages";

  public static String getMessage(String code, Locale locale) {
    return getMessage(code, locale, null);
  }

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

  public static String getMessage(String code) {
    return getMessage(code, LocaleContextHolder.getLocale(), null);
  }

  public static String getMessage(String code, Object... args) {
    return getMessage(code, LocaleContextHolder.getLocale(), args);
  }
}
