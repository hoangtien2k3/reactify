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
package io.hoangtien2k3.commons.utils;

import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * Utility class for creating a {@link ReactiveOAuth2AuthorizedClientManager}
 * instance.
 * <p>
 * The {@code ReactiveOAuth2Utils} class provides a static method to configure
 * and instantiate a {@link ReactiveOAuth2AuthorizedClientManager} with a
 * specific {@link ReactiveOAuth2AuthorizedClientProvider}. This is useful for
 * managing OAuth2 clients in a reactive environment, particularly for scenarios
 * involving client credentials grant types.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class contains a utility method for creating and configuring a
 * {@link ReactiveOAuth2AuthorizedClientManager}. The manager is responsible for
 * handling OAuth2 authorized clients and their respective tokens in a reactive
 * Spring application.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>createAuthorizedClientManager</strong>: Configures and creates an
 * instance of {@link ReactiveOAuth2AuthorizedClientManager} using the provided
 * client registration repository and authorized client service.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>clientRegistrationRepository</code>
 * ({@link ReactiveClientRegistrationRepository}): Repository for managing
 * client registrations. It provides information about client details such as
 * client ID, client secret, and scopes.</li>
 * <li><code>authorizedClientService</code>
 * ({@link ReactiveOAuth2AuthorizedClientService}): Service for managing
 * authorized clients and their tokens. It handles storing and retrieving
 * authorized client information.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A {@link ReactiveOAuth2AuthorizedClientManager} instance configured with the
 * provided client registration repository and authorized client service. This
 * manager is used to manage and authorize OAuth2 clients.
 * </p>
 * </li>
 * <li><strong>Usage:</strong>
 * <p>
 * This method is used to set up an authorized client manager for handling
 * OAuth2 authentication in a reactive application. The manager will use client
 * credentials grant type to obtain access tokens and manage OAuth2 clients.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * ReactiveClientRegistrationRepository clientRegistrationRepository = // obtain or create repository
 * ReactiveOAuth2AuthorizedClientService authorizedClientService = // obtain or create service
 * ReactiveOAuth2AuthorizedClientManager authorizedClientManager =
 *         ReactiveOAuth2Utils.createAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * Ensure that the provided {@link ReactiveClientRegistrationRepository} and
 * {@link ReactiveOAuth2AuthorizedClientService} are correctly configured and
 * initialized before passing them to the method. This setup is crucial for
 * proper OAuth2 client management and token handling.
 * </p>
 */
public class ReactiveOAuth2Utils {

    /**
     * Creates and configures a {@link ReactiveOAuth2AuthorizedClientManager}
     * instance.
     * <p>
     * This method sets up a {@link ReactiveOAuth2AuthorizedClientManager} with a
     * client credentials provider, using the provided
     * {@link ReactiveClientRegistrationRepository} and
     * {@link ReactiveOAuth2AuthorizedClientService}.
     * </p>
     *
     * @param clientRegistrationRepository
     *            The repository for managing client registrations.
     * @param authorizedClientService
     *            The service for managing authorized clients and their tokens.
     * @return A configured {@link ReactiveOAuth2AuthorizedClientManager} instance.
     */
    public static ReactiveOAuth2AuthorizedClientManager createAuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
