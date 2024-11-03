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
package com.reactify.client;

import com.reactify.client.properties.WebClientProperties;
import com.reactify.constants.Constants;
import com.reactify.filter.properties.ProxyProperties;
import com.reactify.filter.webclient.WebClientLoggingFilter;
import com.reactify.filter.webclient.WebClientMonitoringFilter;
import com.reactify.filter.webclient.WebClientRetryHandler;
import com.reactify.util.DataUtil;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

import java.time.Duration;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * The {@code WebClientFactory} class is responsible for creating and managing
 * instances of
 * {@link WebClient} configured
 * with various properties. It integrates with Spring's application context to
 * register the created clients as beans for dependency injection. This class
 * supports OAuth2 authentication, connection pooling, and various customization
 * options such as logging, retry handling, and proxy configuration.
 * </p>
 *
 * <p>
 * The class implements the
 * {@link InitializingBean} interface, which
 * triggers the initialization of web clients after the bean properties have
 * been set. Each web client is created based on the specified
 * {@link WebClientProperties}.
 * </p>
 *
 * <p>
 * The factory allows dynamic creation of multiple
 * {@link WebClient} instances
 * based on provided configurations, enabling flexible and efficient HTTP
 * communication in reactive applications.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Data
public class WebClientFactory implements InitializingBean {
    private final ApplicationContext applicationContext;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private List<WebClientProperties> webClients;

    /**
     * Creates an instance of {@code WebClientFactory} with the specified
     * application context and authorized client manager.
     *
     * @param applicationContext
     *            the application context used to access beans and configuration
     * @param authorizedClientManager
     *            the manager handling OAuth2 client authorizations
     */
    public WebClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.applicationContext = applicationContext;
        this.authorizedClientManager = authorizedClientManager;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method initializes the web clients by calling
     * {@link #initWebClients(List)} with the configured web client properties.
     * </p>
     */
    @Override
    public void afterPropertiesSet() {
        initWebClients(webClients);
    }

    /**
     * <p>
     * Initializes web clients based on the provided list of
     * {@link WebClientProperties}. Each client is
     * created and registered as a singleton bean in the application context.
     * </p>
     *
     * @param webClients
     *            a {@link List} of
     *            {@link WebClientProperties} objects
     *            containing configuration for each web client
     */
    public void initWebClients(List<WebClientProperties> webClients) {
        final ConfigurableListableBeanFactory beanFactory =
                ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        for (WebClientProperties webClientProperties : webClients) {
            var webClient = createNewClient(webClientProperties);
            if (webClient != null) {
                beanFactory.registerSingleton(webClientProperties.getName(), webClient);
            }
        }
    }

    /**
     * <p>
     * Creates a new instance of
     * {@link WebClient} using the
     * provided {@link WebClientProperties}. The
     * client is configured with connection pooling, timeout settings, and
     * additional filters based on the properties specified.
     * </p>
     *
     * @param webClientProperties
     *            a {@link WebClientProperties}
     *            object containing configuration for the web client
     * @return a {@link WebClient}
     *         object configured based on the given properties, or {@code null} if
     *         the properties are invalid
     */
    public WebClient createNewClient(WebClientProperties webClientProperties) {
        if (!isValidProperties(webClientProperties)) {
            log.error("Failed to setup a webClientProperties {}", webClientProperties.getName());
            return null;
        }
        ConnectionProvider connectionProvider = ConnectionProvider.builder(webClientProperties.getName() + "Pool")
                .maxConnections(webClientProperties.getPool().maxSize())
                .pendingAcquireMaxCount(webClientProperties.getPool().maxPendingAcquire())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        webClientProperties.getTimeout().connection())
                .responseTimeout(
                        Duration.ofMillis(webClientProperties.getTimeout().read()))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, 300)
                .option(EpollChannelOption.TCP_KEEPINTVL, 60)
                .option(EpollChannelOption.TCP_KEEPCNT, 8);

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.registerDefaults(true);
                    configurer.defaultCodecs().maxInMemorySize(64 * 1024 * 1024);
                })
                .build();

        Builder exchangeStrategies =
                WebClient.builder().baseUrl(webClientProperties.getAddress()).exchangeStrategies(strategies);
        if (!DataUtil.isNullOrEmpty(webClientProperties.getUsername())) {
            exchangeStrategies.defaultHeader(
                    Constants.Security.AUTHORIZATION,
                    Constants.Security.BEARER + " "
                            + Base64.getEncoder()
                                    .encodeToString((webClientProperties.getUsername() + ":"
                                                    + webClientProperties.getPassword())
                                            .getBytes(UTF_8)));
        }
        if (webClientProperties.getLog().enable()) {
            exchangeStrategies.filter(
                    new WebClientLoggingFilter(webClientProperties.getLog().obfuscateHeaders()));
        }
        if (webClientProperties.getRetry().isEnable()) {
            exchangeStrategies.filter(new WebClientRetryHandler(webClientProperties.getRetry()));
        }
        if (webClientProperties.getMonitoring().isEnable()) {
            exchangeStrategies.filter(new WebClientMonitoringFilter(
                    webClientProperties.getMonitoring().meterRegistry()));
        }
        if (webClientProperties.getProxy().enable()) {
            httpClient = configProxy(httpClient, webClientProperties.getProxy());
        }
        if (webClientProperties.isInternalOauth()) {
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                    new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
            oauth2.setDefaultClientRegistrationId(Constants.Security.DEFAULT_REGISTRATION_ID);
            exchangeStrategies.filter(oauth2);
        }

        List<ExchangeFilterFunction> customFilters = webClientProperties.getCustomFilters();
        if (customFilters != null) {
            for (ExchangeFilterFunction filter : customFilters) {
                exchangeStrategies.filter(filter);
            }
        }
        log.info("Success setup client properties {}", webClientProperties.getName());
        var clientConnector = new ReactorClientHttpConnector(httpClient);

        return exchangeStrategies.clientConnector(clientConnector).build();
    }

    /**
     * <p>
     * Configures the HTTP client to use a proxy if specified in the
     * {@link ProxyProperties}. This method sets up the HTTP and HTTPS proxies with
     * the given host and port.
     * </p>
     *
     * @param httpClient
     *            the original {@link HttpClient} to configure
     * @param proxyConfig
     *            the {@link ProxyProperties} containing proxy configuration
     * @return a {@link HttpClient} object configured with proxy settings
     */
    private HttpClient configProxy(HttpClient httpClient, ProxyProperties proxyConfig) {
        var httpHost = proxyConfig.httpHost();
        var httpPort = proxyConfig.httpPort();
        var httpsHost = proxyConfig.httpsHost();
        var httpsPort = proxyConfig.httpsPort();
        if (!DataUtil.isNullOrEmpty(httpHost) && !DataUtil.isNullOrEmpty(httpPort)) {
            httpClient = httpClient.proxy(
                    proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host(httpHost).port(httpPort));
        }
        if (!DataUtil.isNullOrEmpty(httpsHost) && !DataUtil.isNullOrEmpty(httpsPort)) {
            SslContext sslContext;
            try {
                sslContext = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
            } catch (Exception ex) {
                return httpClient;
            }
            httpClient = httpClient
                    .proxy(proxy ->
                            proxy.type(ProxyProvider.Proxy.HTTP).host(httpsHost).port(httpsPort))
                    .secure(t -> t.sslContext(sslContext));
        }
        return httpClient;
    }

    /**
     * <p>
     * Validates the provided {@link WebClientProperties}. This method checks
     * whether the required fields, such as the name and address, are properly set.
     * </p>
     *
     * @param webClientProperties
     *            the {@link WebClientProperties} to validate
     * @return {@code true} if the properties are valid; {@code false} otherwise
     */
    private boolean isValidProperties(WebClientProperties webClientProperties) {
        return !DataUtil.isNullOrEmpty(webClientProperties.getName())
                && !DataUtil.isNullOrEmpty(webClientProperties.getAddress());
    }
}
