# Reactify

Reactify [a commons Java lib]() with spring boot framework, Supports using keycloak, filter, trace log, cached, minio
server, exception handler, validate and call API with webclient

This README provides quickstart instructions on running [`reactify`]() on bare metal project spring boot.

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=hoangtien2k3_reactify)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)

[![CircleCI](https://circleci.com/gh/hoangtien2k3/reactify.svg?style=svg)](https://app.circleci.com/pipelines/github/hoangtien2k3/reactify)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hoangtien2k3_reactify)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_reactify&metric=ncloc)](https://sonarcloud.io/summary/overall?id=hoangtien2k3_reactify)
![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/reactify?label=latest%20release)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![OpenSSF Best Practices](https://www.bestpractices.dev/projects/9383/badge)](https://www.bestpractices.dev/projects/9383)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/reactify/actions)

## Download
Gradle is the only supported build configuration, so just add the dependency to your project build.gradle file:

⬇️ [Download From Gradle and Maven Central](https://central.sonatype.com/namespace/io.github.hoangtien2k3)

```kotlin
dependencies {
  implementation 'io.github.hoangtien2k3:reactify:$latest'
}
```

```maven
<dependency>
   <groupId>io.github.hoangtien2k3</groupId>
   <artifactId>reactify</artifactId>
   <version>${latest}</version>
</dependency>
```

The latest `reactify` version is: ![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/fw-commons?label=latest%20release)

The latest stable lib `reactify` version is: latestVersion Click [here](https://central.sonatype.com/namespace/io.github.hoangtien2k3) for more information on reactify.

## Getting Started

1. Correct and complete setup to start the program `application.yml` or `application.properties`
   with [CONFIG](src/main/resources/application.yml)

2. The [reference documentation]() includes detailed [installation instructions]() as well as a
   comprehensive [getting started]() guide.

Here is a quick teaser of a complete Spring Boot application in Java:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"io.hoangtien2k3.reactify.*"})
@ImportResource({"classpath*:applicationContext.xml"})
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

<a href="https://github.com/hoangtien2k3/fw-commons/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=hoangtien2k3/reactify" />
</a>

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)

```
Apache License
Copyright (c) 2024 Hoàng Anh Tiến
```
