package io.hoangtien2k3.commons.filter.webclient;// package io.hoangtien2k3.commons.filter.webclient;
//
// import org.springframework.web.reactive.function.client.ClientRequest;
// import org.springframework.web.reactive.function.client.ClientResponse;
// import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
// import org.springframework.web.reactive.function.client.ExchangeFunction;
// import reactor.core.publisher.Mono;
//
// public class ErrorHandlingFilter implements ExchangeFilterFunction {
//    @Override
//    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
//        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//            if(clientResponse.statusCode()!=null &&
// (clientResponse.statusCode().is5xxServerError() ||
// clientResponse.statusCode().is4xxClientError()) ) {
//                return clientResponse.bodyToMono(String.class)
//                        .flatMap(errorBody -> {
//                            return Mono.error(new
// CustomWebClientResponseException(errorBody,clientResponse.statusCode()));
//                        });
//            }else {
//                return Mono.just(clientResponse);
//            }
//        });
//    }
// }
