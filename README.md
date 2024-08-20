# fw-commons

Fw-Commons [a commons Java lib]() with spring boot framework, Supports using keycloak, filter, trace log, cached, minio
server, exception handler, validate and call API with webclient

This README provides quickstart instructions on running [`fw-commons`]() on bare metal project spring boot.

[![License](https://img.shields.io/badge/license-MIT-green)](https://www.opensource.org/licenses/mit-license.php)
[![JDK](https://img.shields.io/badge/jdk-21-green.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Build status](https://github.com/ponfee/commons-core/workflows/build-with-maven/badge.svg)](https://github.com/hoangtien2k3/fw-commons/actions)
[![Maven Central](https://img.shields.io/badge/maven--central-1.1.0-orange.svg?style=plastic&logo=apachemaven)](https://central.sonatype.com/artifact/io.github.hoangtien2k3/fw-commons/1.1.0)
[![Gradle](https://img.shields.io/badge/gradle-1.1.0-orange.svg?style=plastic&logo=apachemaven)](https://central.sonatype.com/artifact/io.github.hoangtien2k3/fw-commons/1.1.0)

### ⬇️ [Download From Gradle and Maven Central](https://central.sonatype.com/artifact/cn.ponfee/commons-core/1.4)

#### Maven

```xml
<dependency>
    <groupId>io.github.hoangtien2k3</groupId>
    <artifactId>fw-commons</artifactId>
    <version>1.1.0</version>
</dependency>
```

#### Gradle

```kotlin
implementation("io.github.hoangtien2k3:fw-commons:1.1.0")
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

## License

This project is licensed under the [`MIT License`](LICENSE).

```text
MIT License
Copyright (c) 2024 Hoàng Anh Tiến
```

## Contributors ✨

<table>
  <tr>
    <td align="center"><a href="https://www.linkedin.com/in/hoangtien2k3/"><img src="https://avatars.githubusercontent.com/u/122768076?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Hoàng Anh Tiến</b></sub></a><br /><a href="https://github.com/hoangtien2k3/news-app/commits?author=hoangtien2k3" title="Code">💻</a> <a href="#maintenance-hoangtien2k3" title="Maintenance">🚧</a> <a href="#ideas-hoangtien2k3" title="Ideas, Planning, & Feedback">🤔</a> <a href="#design-hoangtien2k3" title="Design">🎨</a> <a href="https://github.com/hoangtien2k3/news-app/issues?q=author%hoangtien2k3" title="Bug reports">🐛</a></td>
  </tr>
</table>
