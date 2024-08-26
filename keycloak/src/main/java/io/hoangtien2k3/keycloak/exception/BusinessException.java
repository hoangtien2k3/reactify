package io.hoangtien2k3.keycloak.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String errorCode;
    private String message;
    private Object[] paramsMsg;

    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.paramsMsg = paramsMsg;
        this.message = message;
    }
}
