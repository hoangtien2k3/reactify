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
package io.hoangtien2k3.reactify.filter.properties;

import io.hoangtien2k3.reactify.model.logging.HttpLogRequest;
import io.hoangtien2k3.reactify.model.logging.HttpLogResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * HttpLogProperties class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
@ConfigurationProperties(prefix = "application.http-logging", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogProperties {
    private HttpLogRequest request = new HttpLogRequest();
    private HttpLogResponse response = new HttpLogResponse();
}
