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
