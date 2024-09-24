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
package io.hoangtien2k3.reactify.model.logging;

import lombok.*;

/**
 * <p>LogField class.</p>
 *
 * @author hoangtien2k3
 */
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
