package io.hoangtien2k3.keycloak.client.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.hoangtien2k3.keycloak.DataUtil;
import io.hoangtien2k3.keycloak.client.KeyCloakClient;
import io.hoangtien2k3.keycloak.client.properties.KeycloakClientProperties;
import io.hoangtien2k3.keycloak.config.KeycloakProvider;
import io.hoangtien2k3.keycloak.constants.AuthConstants;
import io.hoangtien2k3.keycloak.constants.CommonErrorCode;
import io.hoangtien2k3.keycloak.constants.Constants;
import io.hoangtien2k3.keycloak.exception.BusinessException;
import io.hoangtien2k3.keycloak.model.AccessToken;
import io.hoangtien2k3.keycloak.model.KeycloakError;
import io.hoangtien2k3.keycloak.model.request.*;
import io.hoangtien2k3.keycloak.model.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class KeyCloakClientImpl implements KeyCloakClient {
    @Qualifier("keycloak")
    private final WebClient keycloak;

    private final KeycloakProvider keycloakProvider;
    private final KeycloakClientProperties keyCloakConfig;

    @Value("${keycloak.serverUrl}")
    private String keycloakUrl;

    @Value("${keycloak.host}")
    private String hostKeycloak;

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
                    .switchIfEmpty(
                            Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "client.id.not.valid")));
        } else {
            formParameters.add(
                    OAuth2ParameterNames.CLIENT_ID, keyCloakConfig.getAuth().getClientId());
            formParameters.add(
                    OAuth2ParameterNames.CLIENT_SECRET, keyCloakConfig.getAuth().getClientSecret());
        }
        return requestToken(formParameters);
    }

    @Override
    public Mono<Optional<AccessToken>> getToken(ClientLogin clientLogin) {
        return keycloakProvider
                .getClientWithSecret(clientLogin.getClientId())
                .flatMap(clientRepresentation -> {
                    if (DataUtil.isNullOrEmpty(clientLogin.getRedirectUri())) {
                        clientLogin.setRedirectUri("http://sso-scontract.vn:8213/callback");
                    }
                    MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
                    formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthConstants.OAuth.AUTHOR_CODE);
                    formParameters.add(OAuth2ParameterNames.CODE, clientLogin.getCode());
                    log.info("RedirectUris {}", clientRepresentation.getRedirectUris());
                    String redirectUrl = clientLogin.getRedirectUri();
                    if (!DataUtil.isNullOrEmpty(redirectUrl)) {
                        formParameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUrl);
                    } else {
                        formParameters.add(
                                OAuth2ParameterNames.REDIRECT_URI,
                                clientRepresentation.getRedirectUris().getFirst());
                    }
                    formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRepresentation.getSecret());
                    formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientLogin.getClientId());
                    return requestToken(formParameters);
                })
                .switchIfEmpty(
                        Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "login.client.id.not.exist")));
    }

    @Override
    public Mono<Optional<AccessToken>> getToken(LoginRequestSync loginRequestSync) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthConstants.OAuth.CLIENT_CREDENTIALS);
        formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, loginRequestSync.getClientSecret());
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, loginRequestSync.getClientId());
        return requestToken(formParameters);
    }

    @Override
    public Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthConstants.OAuth.AUTHOR_CODE);
        formParameters.add(OAuth2ParameterNames.CODE, providerLogin.getCode());
        formParameters.add(OAuth2ParameterNames.REDIRECT_URI, AuthConstants.OAuth.REDIRECT_URI);
        formParameters.add(
                OAuth2ParameterNames.CLIENT_ID, keyCloakConfig.getAuth().getClientId());
        formParameters.add(
                OAuth2ParameterNames.CLIENT_SECRET, keyCloakConfig.getAuth().getClientSecret());
        return requestToken(formParameters);
    }

    @Override
    public Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String clientId = refreshTokenRequest.getClientId();
        if (DataUtil.isNullOrEmpty(clientId)) {
            clientId = keyCloakConfig.getAuth().getClientId();
        }
        return keycloakProvider.getClientWithSecret(clientId).flatMap(clientRepresentation -> {
            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.GRANT_TYPE, OAuth2ParameterNames.REFRESH_TOKEN);
            formParameters.add(OAuth2ParameterNames.REFRESH_TOKEN, refreshTokenRequest.getRefreshToken());
            formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRepresentation.getClientId());
            formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRepresentation.getSecret());
            return requestToken(formParameters);
        });
    }

    @Override
    public Mono<Boolean> logout(LogoutRequest logoutRequest) {
        return keycloakProvider.getClientWithSecret(logoutRequest.getClientId()).flatMap(client -> {
            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(OAuth2ParameterNames.REFRESH_TOKEN, logoutRequest.getRefreshToken());
            formParameters.add(OAuth2ParameterNames.CLIENT_ID, logoutRequest.getClientId());
            formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, client.getSecret());
            return keycloak.post()
                    .uri("/logout")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formParameters))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                            .flatMap(errorBody -> Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                    .bodyToMono(Object.class)
                    .switchIfEmpty(Mono.just(true))
                    .map(response -> true)
                    .doOnError(err -> log.error("Logout error {}", err));
        });
    }

    @Override
    public Mono<List<Permission>> getPermissions(String audience, String token) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthConstants.OAuth.UMA_TICKET);
        formParameters.add("audience", audience);
        formParameters.add("response_mode", AuthConstants.OAuth.RESPONSE_MODE_PERMISSION);
        log.info("formParameters {}", formParameters);
        return keycloak.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formParameters))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.HOST, hostKeycloak)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .bodyToFlux(Map.class)
                .mapNotNull(responseMap -> {
                    if (responseMap == null) return null;
                    Permission permission = new Permission();
                    permission.setRsId((String) responseMap.get("rsid"));
                    permission.setRsName((String) responseMap.get("rsname"));
                    return permission;
                })
                .collectList()
                .doOnError(err -> log.error("Keycloak get token error", err));
    }

    private Mono<Optional<AccessToken>> requestToken(MultiValueMap<String, String> formParameters) {
        log.info("formParameters {}", formParameters);
        return keycloak.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formParameters))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .bodyToMono(AccessToken.class)
                .map(Optional::ofNullable)
                .doOnError(err -> {
                    log.error("Keycloak get token error", err);
                });
    }

    public Mono<List<ClientResource>> getClientResources(String clientId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/clients/" + clientId + "/authz/resource-server/resource";
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(result -> result.stream()
                        .map(record -> {
                            ClientResource res = new ClientResource();
                            res.setId((String) record.get("_id"));
                            res.setName((String) record.get("name"));
                            return res;
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public Mono<List<GroupPolicyRepresentation>> getGroupPolicies(String clientId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/clients/" + clientId
                + "/authz/resource-server/policy/group?permission=false";
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GroupPolicyRepresentation>>() {})
                .onErrorReturn(new ArrayList<GroupPolicyRepresentation>());
    }

    @Override
    public Mono<List<RolePolicyRepresentation>> getRolePolicies(String clientId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/clients/" + clientId
                + "/authz/resource-server/policy/role?permission=false";
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RolePolicyRepresentation>>() {})
                .onErrorReturn(new ArrayList<RolePolicyRepresentation>());
    }

    @Override
    public Mono<String> createUser(EmployeeCreateRequest employeeCreateRequest, String password, String token) {
        CreateUserKeycloakRequest request = new CreateUserKeycloakRequest();
        request.setEmail(employeeCreateRequest.getEmailAccount());
        request.setEnabled(Constants.STATUS.ACTIVE.equals(employeeCreateRequest.getAccountStatus()));
        request.setUsername(employeeCreateRequest.getUsername());
        request.setCredentials(List.of(new CredentialKeycloakRequest("password", password, false)));
        List<String> groupIds = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(employeeCreateRequest.getEmployeePermissionRequestList())) {
            employeeCreateRequest.getEmployeePermissionRequestList().forEach(employeePermissionRequest -> {
                if (!DataUtil.isNullOrEmpty(employeePermissionRequest)
                        && !DataUtil.isNullOrEmpty(employeePermissionRequest.getPermissionGroupList())) {
                    employeePermissionRequest.getPermissionGroupList().forEach(employeePermissionGroup -> {
                        if (!DataUtil.isNullOrEmpty(employeePermissionGroup)) {
                            groupIds.add(employeePermissionGroup.getGroupId());
                        }
                    });
                }
            });
        }

        // neu groupIds null then call createUser
        if (DataUtil.isNullOrEmpty(groupIds)) {
            return createUserInKeycloak(request, token);
        }

        // neu groupIds is not null => get groupNames
        return Flux.fromIterable(groupIds)
                .flatMap(groupId -> getGroupNameById(groupId, token))
                .collectList()
                .flatMap(groupNames -> {
                    if (!DataUtil.isNullOrEmpty(groupNames)) {
                        request.setGroups(groupNames);
                    }
                    return createUserInKeycloak(request, token);
                });
    }

    public Mono<String> createUserInKeycloak(CreateUserKeycloakRequest request, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/";
        return keycloak.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> {
                    String location = response.getHeaders().get("Location").get(0);
                    return Mono.just(location.substring(location.lastIndexOf("/") + 1));
                })
                .doOnError(err -> {
                    log.error("Keycloak create user error", err);
                });
    }

    @Override
    public Mono<Boolean> createRoleUser(
            List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests,
            String userId,
            String clientId,
            String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/role-mappings/clients/" + clientId;

        return keycloak.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(roleUserKeycloakRequests))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> Mono.just(true))
                .doOnError(err -> {
                    log.error("Keycloak create role user error", err);
                });
    }

    @Override
    public Mono<Boolean> updateUser(UpdateUserKeycloakRequest request, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + request.getId();

        return keycloak.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> Mono.just(true))
                .doOnError(err -> {
                    log.error("Keycloak update user error", err);
                });
    }

    @Override
    public Mono<Boolean> removeGroupToUser(String groupId, String userId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/groups/" + groupId;

        return keycloak.delete()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> Mono.just(true))
                .doOnError(err -> {
                    log.error("Keycloak Remove group to user error", err);
                });
    }

    @Override
    public Mono<Boolean> addGroupToUser(String groupId, String userId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/groups/" + groupId;

        return keycloak.put()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> Mono.just(true))
                .doOnError(err -> {
                    log.error("Keycloak Add group to user error", err);
                });
    }

    @Override
    public Mono<Boolean> removeRoleUser(
            List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests,
            String userId,
            String clientId,
            String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/role-mappings/clients/" + clientId;

        return keycloak.method(HttpMethod.DELETE)
                .uri(url)
                .body(BodyInserters.fromValue(roleUserKeycloakRequests))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .toEntity(Object.class)
                .flatMap(response -> Mono.just(true))
                .doOnError(err -> {
                    log.error("Keycloak Remove role user error", err);
                });
    }

    @Override
    public Mono<List<RoleDTO>> getRoleNameByUserIdAndClientId(String userId, String clientId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/role-mappings/clients/" + clientId
                + "/composite";
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "keycloakClient.failed"))))
                .bodyToMono(String.class)
                .map(response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<RoleDTO>>() {}.getType();
                    log.info("response when login {}", response);
                    List<RoleDTO> roleDTOS = gson.fromJson(response, listType);
                    return roleDTOS;
                })
                .doOnError(err -> log.error("Call keycloak error ", err))
                .onErrorResume(throwable -> Mono.error(
                        new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Call keycloak that bai")));
    }

    @Override
    public Mono<UserRepresentation> getUser(String userId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId;

        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .bodyToMono(UserRepresentation.class)
                .doOnError(err -> {
                    log.error("Get delete user error", err);
                });
    }

    public Mono<List<String>> getResourcesByClient(String clientId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/clients/" + clientId
                + "/authz/resource-server/resource?deep=true&first=0&max=500";
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .doOnError(err -> {
                    log.error("Error when get all resource ", err);
                })
                .map(result -> result.stream()
                        .map(record -> (String) record.get("name"))
                        .collect(Collectors.toList()));
    }

    @Override
    public Mono<String> getGroupNameById(String groupId, String token) {
        String url = keycloakUrl + "/realms/ezbuy-server/groups/" + groupId;
        return keycloak.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(GroupRepresentation.class)
                .doOnError(err -> {
                    log.error("Error when get group by id ", err);
                })
                .map(GroupRepresentation::getName);
    }

    // add role for dich vu cho userId
    @Override
    public Mono<Boolean> addRoleForUserInClientId(
            String clientId, String token, RoleRepresentation roleRepresentation, String userId) {
        List<RoleRepresentation> roleRepresentations = new ArrayList<>();
        roleRepresentations.add(roleRepresentation);
        String url = keycloakUrl + "/realms/ezbuy-server/users/" + userId + "/role-mappings/clients/" + clientId;
        return keycloak.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleRepresentations)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(KeycloakError.class)
                        .flatMap(errorBody -> Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, errorBody.getMessage()))))
                .bodyToMono(Object.class)
                .switchIfEmpty(Mono.just(true))
                .map(response -> true)
                .doOnError(err -> log.error("Logout error {}", err));
    }
}
