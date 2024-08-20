package io.hoangtien2k3.commons.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnRetryableException extends BusinessException {

    public UnRetryableException(String errorCode, String message) {
        super(errorCode, message);
    }
}
