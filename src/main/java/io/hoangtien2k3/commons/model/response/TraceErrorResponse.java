package io.hoangtien2k3.commons.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an error response with trace information, extending DataResponse.
 *
 * @param <T> the type of the response data
 */
@Getter
@Setter
public class TraceErrorResponse<T> extends DataResponse<T> {
    private String requestId;

    /**
     * Constructs a TraceErrorResponse with error code, message, data, and request ID.
     *
     * @param errorCode the error code to be included in the response
     * @param message the message to be included in the response
     * @param data the data to be included in the response
     * @param requestId the request ID to be included in the response
     */
    public TraceErrorResponse(String errorCode, String message, T data, String requestId) {
        super(errorCode, message, data);
        this.requestId = requestId;
    }
}
