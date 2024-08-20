/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.filter.webclient;

import io.hoangtien2k3.commons.utils.ReactiveOAuth2Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
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
