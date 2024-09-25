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
package io.hoangtien2k3.reactify.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import io.hoangtien2k3.reactify.constants.Constants;
import io.hoangtien2k3.reactify.filter.properties.ProxyProperties;
import io.hoangtien2k3.reactify.filter.webclient.WebClientLoggingFilter;
import io.hoangtien2k3.reactify.filter.webclient.WebClientMonitoringFilter;
import io.hoangtien2k3.reactify.filter.webclient.WebClientRetryHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

/**
 * <p>
 * WebClientFactory class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@RequiredArgsConstructor
@Data
public class WebClientFactory implements InitializingBean {
    private final ApplicationContext applicationContext;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private List<WebClientProperties> webClients;

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() {
        initWebClients(webClients);
    }

    /**
     * <p>
     * initWebClients.
     * </p>
     *
     * @param webClients
     *            a {@link java.util.List} object
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
     * createNewClient.
     * </p>
     *
     * @param webClientProperties
     *            a
     *            {@link io.hoangtien2k3.reactify.client.properties.WebClientProperties}
     *            object
     * @return a {@link org.springframework.web.reactive.function.client.WebClient}
     *         object
     */
    public WebClient createNewClient(WebClientProperties webClientProperties) {
        if (!isValidProperties(webClientProperties)) {
            log.error("Failed to setup a webClientProperties {}", webClientProperties.getName());
            return null;
        }
        ConnectionProvider connectionProvider = ConnectionProvider.builder(webClientProperties.getName() + "Pool")
                .maxConnections(webClientProperties.getPool().getMaxSize())
                .pendingAcquireMaxCount(webClientProperties.getPool().getMaxPendingAcquire())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        webClientProperties.getTimeout().getConnection())
                .responseTimeout(
                        Duration.ofMillis(webClientProperties.getTimeout().getRead()))
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
        if (webClientProperties.getLog().isEnable()) {
            exchangeStrategies.filter(
                    new WebClientLoggingFilter(webClientProperties.getLog().getObfuscateHeaders()));
        }
        if (webClientProperties.getRetry().isEnable()) {
            exchangeStrategies.filter(new WebClientRetryHandler(webClientProperties.getRetry()));
        }
        if (webClientProperties.getMonitoring().isEnable()) {
            exchangeStrategies.filter(new WebClientMonitoringFilter(
                    webClientProperties.getMonitoring().getMeterRegistry()));
        }
        if (webClientProperties.getProxy().isEnable()) {
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

    private HttpClient configProxy(HttpClient httpClient, ProxyProperties proxyConfig) {
        var httpHost = proxyConfig.getHttpHost();
        var httpPort = proxyConfig.getHttpPort();
        var httpsHost = proxyConfig.getHttpsHost();
        var httpsPort = proxyConfig.getHttpsPort();
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

    private boolean isValidProperties(WebClientProperties webClientProperties) {
        return !DataUtil.isNullOrEmpty(webClientProperties.getName())
                && !DataUtil.isNullOrEmpty(webClientProperties.getAddress());
    }
}
