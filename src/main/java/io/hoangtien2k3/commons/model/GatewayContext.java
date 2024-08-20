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
package io.hoangtien2k3.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@ToString
public class GatewayContext {
  public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

  /** whether read request data */
  protected Boolean readRequestData = true;

  /** whether read response data */
  protected Boolean readResponseData = true;

  /** cache json body */
  protected String requestBody;

  /** cache Response Body */
  protected Object responseBody;

  /** request headers */
  protected HttpHeaders requestHeaders;

  /** cache form data */
  protected MultiValueMap<String, String> formData;

  /** cache all request data include:form data and query param */
  protected MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);

  /** Gateway Start time of request */
  protected Long startTime;
}
