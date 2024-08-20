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
package io.hoangtien2k3.commons.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an error response with trace information, extending DataResponse.
 *
 * @param <T>
 *            the type of the response data
 */
@Getter
@Setter
public class TraceErrorResponse<T> extends DataResponse<T> {
  private String requestId;

  /**
   * Constructs a TraceErrorResponse with error code, message, data, and request
   * ID.
   *
   * @param errorCode
   *            the error code to be included in the response
   * @param message
   *            the message to be included in the response
   * @param data
   *            the data to be included in the response
   * @param requestId
   *            the request ID to be included in the response
   */
  public TraceErrorResponse(String errorCode, String message, T data, String requestId) {
    super(errorCode, message, data);
    this.requestId = requestId;
  }
}
