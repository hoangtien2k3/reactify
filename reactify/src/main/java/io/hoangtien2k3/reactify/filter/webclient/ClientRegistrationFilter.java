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
package io.hoangtien2k3.reactify.filter.webclient;

import io.hoangtien2k3.reactify.utils.ReactiveOAuth2Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * <p>ClientRegistrationFilter class.</p>
 *
 * @author hoangtien2k3
 */
@Configuration
public class ClientRegistrationFilter {
    /**
     * <p>authorizedClientManager.</p>
     *
     * @param clientRegistrationRepository a {@link org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository} object
     * @param authorizedClientService a {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService} object
     * @return a {@link org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager} object
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        return ReactiveOAuth2Utils.createAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }
}
