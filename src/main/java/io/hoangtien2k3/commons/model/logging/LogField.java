package io.hoangtien2k3.commons.model.logging;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogField {
    private String traceId;
    private String requestId;
    private String service;
    private Long duration;
    private String logType;
    private String actionType;
    private Long startTime;
    private Long endTime;
    private String clientAddress;
    private String title;
    private String inputs;
    private String response;
    private String result;
}
