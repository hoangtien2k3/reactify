package io.hoangtien2k3.commons.filter.webclient;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@Data
@RequiredArgsConstructor
public class WebClientMonitoringFilter implements ExchangeFilterFunction {
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";
    private final MeterRegistry meterRegistry;
    //    private WebClientExchangeTagsProvider tagsProvider = new DefaultWebClientExchangeTagsProvider();

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(METRICS_WEBCLIENT_START_TIME);
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        //                Iterable<Tag> tags = tagsProvider.tags(clientRequest, clientResponse,
                        // throwable);
                        //                Timer.builder("http.client.requests ")
                        //                        .tags(tags)
                        //                        .description("Timer of WebClient operation")
                        //                        .publishPercentiles(0.95, 0.99)
                        //                        .register(meterRegistry)
                        //                        .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                        //                log.info("Monitoring webClient API {}: {} s", tags, (double)
                        // (System.nanoTime() - startTime) / Math.pow(10, 9));
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }
}
