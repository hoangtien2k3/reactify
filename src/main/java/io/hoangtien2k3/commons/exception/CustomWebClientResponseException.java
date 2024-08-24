package io.hoangtien2k3.commons.exception;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
@Setter
public class CustomWebClientResponseException extends WebClientResponseException {

    private final String errorBody;
    private final HttpStatus statusCode;

    public CustomWebClientResponseException(String errorBody, HttpStatus statusCode) {
        super(statusCode.value(), statusCode.getReasonPhrase(), null, errorBody.getBytes(), null);
        this.errorBody = errorBody;
        this.statusCode = statusCode;
    }

    @NotNull
    @Override
    public String getMessage() {
        return String.format(
                "CustomWebClientResponseException: HTTP %d %s - %s",
                statusCode.value(), statusCode.getReasonPhrase(), errorBody);
    }
}
