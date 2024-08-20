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

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Objects;

/**
 * Utility class for handling HTTP requests. Provides methods to extract the
 * real IP address from request headers and to generate offline cache keys.
 */
public class RequestUtils {

  // Define constants for header names
  private static final String HEADER_X_ORIGINAL_FORWARDED_FOR = "x-original-forwarded-for";
  private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
  private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
  private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
  private static final String HEADER_X_REAL_IP = "X-Real-IP";
  private static final String UNKNOWN = "unknown";

  /**
   * Retrieves the real IP address from the request headers. Checks various
   * headers to find the IP address, falling back to the remote address if
   * necessary.
   *
   * @param request
   *            the ServerHttpRequest from which to extract the IP address
   * @return the real IP address as a String
   */
  public static String getIpAddress(ServerHttpRequest request) {
    HttpHeaders headers = request.getHeaders();
    String ip = headers.getFirst(HEADER_X_ORIGINAL_FORWARDED_FOR);
    if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
      ip = headers.getFirst(HEADER_X_FORWARDED_FOR);
    }
    if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
      ip = headers.getFirst(HEADER_PROXY_CLIENT_IP);
    }
    if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
      ip = headers.getFirst(HEADER_WL_PROXY_CLIENT_IP);
    }
    if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
      ip = headers.getFirst(HEADER_X_REAL_IP);
    }
    if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
      ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
    }
    if (ip != null && ip.length() > 15 && ip.contains(",")) {
      ip = ip.substring(0, ip.indexOf(","));
    }
    return ip;
  }

  /**
   * Generates an offline cache key using the provided IP address and port.
   *
   * @param ip
   *            the IP address to be used in the cache key
   * @param port
   *            the port number to be used in the cache key
   * @return the generated cache key as a String, or null if the IP address is
   *         null
   */
  public static String getOfflineCacheKey(String ip, int port) {
    if (ip != null) {
      return ip + ":" + port;
    }
    return null;
  }
}
