package io.hoangtien2k3.commons.filter.webclient;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TraceIdWebClientFilter implements ExchangeFilterFunction {
    private final Tracer tracer;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return Mono.defer(() -> {
            var span = tracer.currentSpan();
            if (span != null) {
                ClientRequest modifiedRequest = ClientRequest.from(request)
                        .header("X-B3-TRACE-ID", span.context().traceIdString())
                        .build();
                return next.exchange(modifiedRequest);
            } else {
                return next.exchange(request);
            }
        });
    }
}
