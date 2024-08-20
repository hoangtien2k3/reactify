package io.hoangtien2k3.commons.filter.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class WebClientLoggingFilter implements ExchangeFilterFunction {

    private static final String OBFUSCATE_HEADER = "xxxxx";
    private final List<String> obfuscateHeader;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        log.info("Start Call API - Method: {} {}", request.method(), request.url());
        if (request.headers().getContentLength() > 0) {
            log.info("body ", request.body());
        }
        if (log.isDebugEnabled()) {
            request.headers()
                    .forEach((name, values) -> values.forEach(value -> log.debug(
                            "Request header: {}={}", name, obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
        }

        return next.exchange(request).flatMap(clientResponse -> {
            if (log.isDebugEnabled()) {
                clientResponse
                        .headers()
                        .asHttpHeaders()
                        .forEach((name, values) -> values.forEach(value -> log.debug(
                                "Response header: {}={}",
                                name,
                                obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
            }
            return Mono.just(clientResponse);
        });
    }
}
