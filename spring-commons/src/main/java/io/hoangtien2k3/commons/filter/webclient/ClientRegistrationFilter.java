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
package io.hoangtien2k3.commons.filter.webclient;

import io.hoangtien2k3.commons.utils.ReactiveOAuth2Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * {@code ClientRegistrationFilter} is a configuration class that provides the
 * necessary beans for managing OAuth2 authorized clients in a reactive
 * application.
 * <p>
 * This configuration sets up a {@link ReactiveOAuth2AuthorizedClientManager}
 * bean, which is responsible for managing OAuth2 authorized clients. The
 * manager is created using the provided
 * {@link ReactiveClientRegistrationRepository} and
 * {@link ReactiveOAuth2AuthorizedClientService}.
 * </p>
 */
@Configuration
public class ClientRegistrationFilter {

    /**
     * Creates a {@link ReactiveOAuth2AuthorizedClientManager} bean.
     * <p>
     * This method uses the
     * {@link ReactiveOAuth2Utils#createAuthorizedClientManager} utility to
     * configure and create the authorized client manager. The manager is
     * responsible for handling the OAuth2 authorization process and maintaining
     * authorized client information.
     * </p>
     *
     * @param clientRegistrationRepository
     *            the repository for managing client registrations
     * @param authorizedClientService
     *            the service for managing authorized clients
     * @return a {@link ReactiveOAuth2AuthorizedClientManager} instance
     */
    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        return ReactiveOAuth2Utils.createAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
    }
}
