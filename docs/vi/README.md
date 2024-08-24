## Hướng Dẫn Thiết Lập File Resource:

- `application.yml` and `application.properties`

## Thiết lập ghi đè Bean
```yml
spring:
  main:
    web-application-type: reactive
    allow-bean-definition-overriding: true
```

## Thiết lập Security OAuth2 với Keycloak

```yml
spring:
  security:
    oauth2:
      client:
        provider:
          oidc:
            token-uri: http://localhost:8080/realms/ezbuy-server/protocol/openid-connect/token
        registration:
          oidc:
            client-id: ezbuy-client
            client-secret: mI92QDfvi20tZgFtjpRAPWu8TR6eMHmw
            authorization-grant-type: password #client_credentials
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:8080/realms/ezbuy-server/protocol/openid-connect/certs
      keycloak:
        client-id: ezbuy-client
```

## Thiết lập Unauthenticated Endpoints Config

```yml
application:
  http-logging:
    request:
      enable: true
      header: true
      param: true
      body: true
    response:
      enable: true
      body: true
  whiteList:
    - uri: /actuator/health
      methods:
        - GET
    - uri: /v1/demo/post
      methods:
        - POST
    - uri: /v1/demo/put
      methods:
        - PUT
    - uri: /v1/demo/delete
      methods:
        - DELETE
    - uri: /v1/demo/head
      methods:
        - HEAD
    - uri: /v1/demo/options
      methods:
        - OPTIONS
```

## Thiết lập client connect:

```yml
client:
  keycloak:
    address: http://localhost:8080/realms/ezbuy-server/protocol/openid-connect
    name: keycloak
    auth:
      client-id: ezbuy-client
      client-secret: mI92QDfvi20tZgFtjpRAPWu8TR6eMHmw
```

## Thiết lập minio

```yml
minio:
  bucket: ezbuy-bucket
  enabled: true
  baseUrl: http://localhost:9000
  publicUrl: http://localhost:9000/ezbuy-bucket
  accessKey: 4DoaZ0KdzpXdDlVK104t
  secretKey: nuRiQUIJNVygMOHhmtR4LT1etAa7F8PQOsRGP5oj
  private:
    bucket: ezbuy-private
```

## Thiết lập hash password
```yml
hashing-password:
  public-key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw5e+CZbXbC8SRbrp+X9O4QRrKQWRu2zJpYGmGtP3bdS6NtsVBaJz2hHwF1KeEzZTnlW/jy7NOYYR5Rp5nD1RfAJooxZxgO7BcZZKbsnBNe+KLUQrxNOgkx2ZpMs60UdzToz0IvIN5L5NFaiKq5/WwRpG4aIM8iV/ME1L/QQw0FDD7m/2PUxDlANXJl5hZgXfZ1hpo/31HXkBofJTWJ/MdTAnxR4u5y+mXYUsf9GmCmA5exPpPZZv1qT1D7AWv0CXi5Ftd2ylbD6jAtF5MC1ngO4FEGZh9uTwXQh2x8FJmc2U7KKTQMWuhbXtkANVVLMg+GPx7lTxDTwQpKmldf5UDuuzcBQIDAQAB
```

## Thiết lập proxy
```yml
proxy-client:
  host: 10.207.156.52
  port: 3128
```
