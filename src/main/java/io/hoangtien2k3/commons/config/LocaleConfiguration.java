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
package io.hoangtien2k3.commons.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

/**
 * Configuration class for locale and message source settings in the
 * application.
 * <p>
 * This configuration class sets up the locale resolver and message source to
 * handle internationalization (i18n) and localization in a Spring Boot
 * application.
 * </p>
 */
@Configuration
public class LocaleConfiguration {

    /**
     * Configures the {@link LocaleContextResolver} bean.
     * <p>
     * This bean is responsible for resolving the locale based on the
     * Accept-Language header in the HTTP request.
     * </p>
     * <p>
     * In this configuration, the default locale is set to Vietnamese (vi).
     * </p>
     *
     * @return an instance of {@link LocaleContextResolver} configured with the
     *         default locale
     */
    @Bean("localeContextResolver2")
    public LocaleContextResolver localeContextResolver() {
        var resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("vi")); // Set default locale to Vietnamese
        return resolver;
    }

    /**
     * Configures the {@link ReloadableResourceBundleMessageSource} bean.
     * <p>
     * This bean is used to load and reload message bundles for internationalization
     * purposes.
     * </p>
     * <p>
     * The message source is configured to use UTF-8 encoding and loads messages
     * from the classpath location "/i18n/messages".
     * </p>
     * <p>
     * Messages can be retrieved using their codes, and the code itself is used as
     * the default message if no translation is found.
     * </p>
     *
     * @return an instance of {@link ReloadableResourceBundleMessageSource}
     *         configured for loading messages
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource rs = new ReloadableResourceBundleMessageSource();
        rs.setDefaultEncoding("UTF-8"); // Set encoding to UTF-8
        rs.setBasenames("classpath:/i18n/messages"); // Location of message bundles
        rs.setUseCodeAsDefaultMessage(true); // Use message code as default message if no translation is found
        return rs;
    }
}
