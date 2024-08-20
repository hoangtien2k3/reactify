// package io.hoangtien2k3.commons.filter.webclient;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
// import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//
// @Configuration
// public class ClientRegistrationFilter {
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
//            ReactiveClientRegistrationRepository clientRegistrationRepository,
//            ReactiveOAuth2AuthorizedClientService authorizedClientService) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                        .clientCredentials()
//                        .build();
//
//        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//                new
// AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository,
// authorizedClientService);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }
// }
