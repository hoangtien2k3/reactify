<h3 align="center">
<img src="docs/images/reactify_banner.png" alt="Ezbuy" width="300" />

<a href="https://github.com/hoangtien2k3/reactify/blob/main/docs/en/README.md">📚Docs</a> |
<a href="https://github.com/hoangtien2k3/reactify/blob/main/docs/en/README.md">💬Chat</a> |
<a href="https://github.com/hoangtien2k3/reactify/blob/main/docs/en/README.md">✨Live Demo</a>
</h3>

##

Reactify [a commons Java lib]() with spring boot framework, Supports using keycloak, filter, trace log, cached, minio
server, exception handler, validate and call API with webclient

This README provides quickstart instructions on running [`reactify`]() on bare metal project spring boot.

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)

[![CircleCI](https://circleci.com/gh/hoangtien2k3/reactify.svg?style=svg)](https://app.circleci.com/pipelines/github/hoangtien2k3/reactify)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=ncloc)](https://sonarcloud.io/summary/overall?id=hoangtien2k3_reactify)
[![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest%20release)](https://mvnrepository.com/artifact/io.github.hoangtien2k3/reactify)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![OpenSSF Best Practices](https://www.bestpractices.dev/projects/9383/badge)](https://www.bestpractices.dev/projects/9383)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/reactify/actions)

## Getting Started

Gradle is the only supported build configuration, so just add the dependency to your project build.gradle file:

⬇️ Download Gradle and Maven

```kotlin
dependencies {
    implementation("io.github.hoangtien2k3:reactify-core:1.1.7")
}
```

```maven
<dependency>
   <groupId>io.github.hoangtien2k3</groupId>
   <artifactId>reactify-core</artifactId>
   <version>1.1.7</version>
</dependency>
```

The latest `reactify` version
is: [![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest)](https://mvnrepository.com/artifact/io.github.hoangtien2k3/reactify)

The latest stable lib `reactify` version is: latestVersion
Click [here](https://central.sonatype.com/namespace/io.github.hoangtien2k3) for more information on reactify.

1. Correct and complete setup to start the program `application.yml` or `application.properties`
   with [CONFIG](src/main/resources/application.yml)

2. The [reference documentation]() includes detailed [installation instructions]() as well as a
   comprehensive [getting started]() guide.

Here is a quick teaser of a complete Spring Boot application in Java:

### Start Using `reactify-core`

```java

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.reactify.*",
        "com.example.myproject"
})
public class Example {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
```

## Using Lib Reactify-Core Demo:

1. `LocalCache`

```java

@LocalCache(durationInMinute = 30, maxRecord = 10000, autoCache = true)
public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
    return optionSetValueRepository.findByOptionSetCode(optionSetCode).collectList();
}
```

2. `Keycloak`

application.yml

```yml
spring:
  security:
    oauth2:
      client:
        provider:
          oidc:
            token-uri: ${keycloak.serverUrl}/realms/${keycloak.realm}/protocol/openid-connect/token
        registration:
          oidc:
            client-id: ${keycloak.clientId}
            client-secret: ${keycloak.clientSecret}
            authorization-grant-type: ${keycloak.grantType} #password || #client_credentials
      resourceserver:
        jwt:
          jwk-set-uri: ${keycloak.serverUrl}/realms/${keycloak.realm}/protocol/openid-connect/certs
      keycloak:
        client-id: ${keycloak.clientId}

```

```java

@Override
public Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest) {
    MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
    formParameters.add(OAuth2ParameterNames.GRANT_TYPE, OAuth2ParameterNames.PASSWORD);
    formParameters.add(OAuth2ParameterNames.USERNAME, loginRequest.getUsername());
    formParameters.add(OAuth2ParameterNames.PASSWORD, loginRequest.getPassword());
    String clientId = loginRequest.getClientId();
    if (!DataUtil.isNullOrEmpty(clientId)) {
        return keycloakProvider
                .getClientWithSecret(clientId)
                .flatMap(clientRepresentation -> {
                    formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientId);
                    formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRepresentation.getSecret());
                    return requestToken(formParameters);
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "client.id.not.valid")));
    } else {
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, keyCloakConfig.getAuth().clientId());
        formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, keyCloakConfig.getAuth().clientSecret());
    }
    return requestToken(formParameters);
}
```

3. Call Api Using BaseRest and BaseSoap Client

```java
public Mono<String> getEmailsByUsername(String username) {
    var payload = new LinkedMultiValueMap<>();
    payload.set("username", username);
    return SecurityUtils.getTokenUser().flatMap(token -> {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return baseRestClient
                .get(authClient, "/user/keycloak", headers, payload, String.class)
                .map(response -> {
                    Optional<?> optionalEmail = (Optional<?>) response;
                    return DataUtil.safeToString(optionalEmail.orElse(null));
                });
    });
}
```

## Contributing

If you would like to contribute to the development of this project, please follow our contribution guidelines.

![Alt](https://repobeats.axiom.co/api/embed/31a861bf21d352264c5c122808407abafb97b0ef.svg "Repobeats analytics image")

## Star History

<a href="https://star-history.com/#hoangtien2k3/fw-commons&Timeline">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=hoangtien2k3/fw-commons&type=Timeline" />
 </picture>
</a>

## Contributors ✨

<a href="https://github.com/hoangtien2k3/reactify/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=hoangtien2k3/reactify" />
</a>

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

```
Apache License
Copyright (c) 2024 Hoàng Anh Tiến
```

## Lead to This Project 🌈

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://www.linkedin.com/in/hoangtien2k3/"><img src="https://avatars.githubusercontent.com/u/122768076?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Hoàng Anh Tiến</b></sub></a><br /><a href="https://github.com/hoangtien2k3/news-app/commits?author=hoangtien2k3" title="Code">💻</a> <a href="#maintenance-hoangtien2k3" title="Maintenance">🚧</a> <a href="#ideas-hoangtien2k3" title="Ideas, Planning, & Feedback">🤔</a> <a href="#design-hoangtien2k3" title="Design">🎨</a> <a href="https://github.com/hoangtien2k3/news-app/issues?q=author%hoangtien2k3" title="Bug reports">🐛</a></td>
  </tr>

</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->
