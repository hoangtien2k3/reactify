![fw-commons](https://socialify.git.ci/hoangtien2k3/fw-commons/image?description=1&descriptionEditable=Java%20Library%20for%20Reactive%20Backend%20Processing%20with%20WebFlux&font=KoHo&forks=1&issues=1&language=1&logo=https%3A%2F%2Fpbs.twimg.com%2Fmedia%2FCcZdW37UcAA9DZz%3Fformat%3Dpng%26name%3Dsmall&pattern=Floating%20Cogs&pulls=1&stargazers=1&theme=Auto)

# fw-commons

Fw-Commons [a commons Java lib]() with spring boot framework, Supports using keycloak, filter, trace log, cached, minio
server, exception handler, validate and call API with webclient

This README provides quickstart instructions on running [`fw-commons`]() on bare metal project spring boot.

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/project/overview?id=hoangtien2k3_fw-commons)

[![CircleCI](https://circleci.com/gh/hoangtien2k3/fw-commons.svg?style=svg)](https://app.circleci.com/pipelines/github/hoangtien2k3/fw-commons)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=hoangtien2k3_fw-commons&metric=ncloc)](https://sonarcloud.io/summary/overall?id=hoangtien2k3_fw-commons)
![GitHub Release](https://img.shields.io/github/v/release/hoangtien2k3/fw-commons?label=latest%20release)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![OpenSSF Best Practices](https://bestpractices.coreinfrastructure.org/projects/9383/badge)](https://bestpractices.coreinfrastructure.org/projects/9383)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/fw-commons/actions)

#### ⬇️ [Download From Gradle and Maven Central](https://central.sonatype.com/namespace/io.github.hoangtien2k3)

#### Gradle

```kotlin

def latest = "1.1.4" // or check the latest from maven central repository

//fw-commons
implementation("io.github.hoangtien2k3:fw-commons:$latest")

//spring-commons
implementation("io.github.hoangtien2k3:spring-commons:$latest")

//utils
implementation("io.github.hoangtien2k3:utils:$latest")

//data-utils
implementation("io.github.hoangtien2k3:data-utils:$latest")

//exception-handling
implementation("io.github.hoangtien2k3:exception-handling:$latest")
```


## Installation and Getting Started

1. Correct and complete setup to start the program `application.yml` or `application.properties`
   with [CONFIG](src/main/resources/application.yml)

2. The [reference documentation]() includes detailed [installation instructions]() as well as a
   comprehensive [getting started]() guide.

Here is a quick teaser of a complete Spring Boot application in Java:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"io.hoangtien2k3.commons.*"})
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
  <img src="https://contrib.rocks/image?repo=hoangtien2k3/fw-commons" />
</a>

## License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
