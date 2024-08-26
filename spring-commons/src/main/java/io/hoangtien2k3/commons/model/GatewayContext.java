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
