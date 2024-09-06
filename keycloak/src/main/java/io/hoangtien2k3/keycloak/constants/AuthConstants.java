package io.hoangtien2k3.keycloak.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AuthConstants {
    public static final long[] EMPLOYEE_CODE_MAX = {0L};
    public static final String SUCCESS = "common.success";
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;

    public static final class Notification {
        public static final String SEVERITY = "NORMAL";
        public static final String CONTENT_TYPE = "text/plain";
        public static final String CATEGORY_TYPE = "ANNOUNCEMENT"; // THONG_BAO
        public static final String CHANNEL_TYPE = "EMAIL";
    }

    public static final class Message {
        public static final String EMAIL_INVALID = "dto.email.invalid";
        public static final String DATA_NOT_EXIST = "data.notExist";
        public static final String EMPLOYEE_INPUT_NOT_NULL = "employee.input.notNull";
        public static final String EMPLOYEE_CODE = "employee.code";
        public static final String DATA_IS_EXISTS = "data.input.is.exists";
        public static final String USER_NAME = "login.account";
    }

    public static final class OAuth {
        public static final String AUTHOR_CODE = "authorization_code";
        public static final String UMA_TICKET = "urn:ietf:params:oauth:grant-type:uma-ticket";
        public static final String RESPONSE_MODE_PERMISSION = "permissions";
        public static final String REDIRECT_URI = "http://10.207.252.223/callback";
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer ";
        public static final String CLIENT_CREDENTIALS =
                "client_credentials"; // grant_type de lay token by clientId va clientSecret
    }

    public static final class MySign {
        public static final Integer SIGH_HASH_SUCCESS = 1;
        public static final Integer SIGH_HASH_WAIT = 4000;
        public static final int SIGN_HASH_ASYNC = 1;
        public static final String OID_NIST_SHA1 = "1.3.14.3.2.26";
        public static final String OID_NIST_SHA256 = "2.16.840.1.101.3.4.2.1";
        public static final String OID_RSA_RSA = "1.2.840.113549.1.1.1";
    }

    public static final class PositionCode {
        public static final String OWNER = "OWNER";
        public static final String REPRESENTATIVE = "REPRESENTATIVE";
        public static final String LEADER = "LEADER";
    }

    public static final class TenantType {
        public static final String ORGANIZATION = "ORGANIZATION";
        public static final String INDIVIDUAL = "INDIVIDUAL";
        public static final String ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
    }

    public static final class UserAttributes {
        public static final String INDIVIDUAL_ID = "individual_id";
    }

    public static final class OrganizationState {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
        public static final List<Integer> ALLOW_ORGANIZATION_STATE = List.of(INACTIVE, ACTIVE);
    }

    public static final class IDType {
        public static final String MST = "MST";
        public static final String GPKD = "GPKD";
    }

    public static final class SystemPolicy {
        public static final String SYSTEM_FULL_PERMISSION = "SYSTEM_ALL_PERMISSION";
    }

    public static final class ConfigSync {
        public static final Integer ALLOW = 1;
        public static final String ORGANIZATION_TYPE = "organization";
        public static final String INDIVIDUAL_TYPE = "individual";
        public static final String ORGANIZATION_UNIT_TYPE = "organizationUnit";
        public static final String POLICY = "policy";
        public static final String PASSWORD_TYPE = "password";
        public static final String PREFIX_BEAN = "syncDataService-";
    }

    public static final class HashedPasswordType {
        public static final String HUB_SME = "PRIVATE_HUB";
        public static final String KEYCLOAK = "KEYCLOAK";
    }

    public static final class TenantIdentify {
        public static final Integer PRIMARY_IDENTIFY = 1;
        public static final String ID_TYPE_MST = "MST";
    }

    public static final class SyncServiceType {
        public static final String TYPE_SPECIFIC = "SPECIFIC";
        public static final String TYPE_ALL_BY_ORG = "ALL_BY_ORG";
    }

    public static final class SyncObjectType {
        public static final String TYPE_SPECIFIC = "SPECIFIC";
        public static final String TYPE_ALL = "ALL";
        public static final String EVENT = "EVENT";
        public static final String FIRST_CONNECTED = "FIRST_CONNECTED";
    }

    public static final class DataSyncType {
        public static final String ORGANIZATION_TYPE = "ORGANIZATION";
        public static final String INDIVIDUAL_TYPE = "INDIVIDUAL";
        public static final String ORGANIZATION_UNIT_TYPE = "ORGANIZATION_UNIT";
        public static final String POLICY_TYPE = "POLICY";
        public static final String PASSWORD_TYPE = "PASSWORD";
        public static final String CHANGE_PASSWORD_TYPE = "CHANGE_PASSWORD";
    }

    public static final class OrganizationUnitCodeDefault {
        public static final String PB1 = "PB1";
    }

    public static final class UnitTypeCode {
        public static final String DEPARTMENT = "DEPARTMENT";
    }

    public static final class GroupStatus {
        public static final String ACTIVE = "1";
        public static final String INACTIVE = "0";
    }

    public static final class ALGORITHM {
        public static final Map<String, String> ALGORITHM_MAP = new HashMap<>() {
            {
                put("pbkdf2-sha256", "PBKDF2WithHmacSHA256");
            }
        };
    }

    public static final class System {
        public static final String SME_HUB = "SME_HUB";
    }

    public static final class Proxy {
        // request enable proxy
        public static final Integer ENABLE = 1;
    }

    public static final class Protocol {
        // http
        public static final Integer HTTP = 0;

        // https
        public static final Integer HTTPS = 1;
    }

    public static final class ClientName {
        public static final String HUB_SME = "web-client";
        public static final String EZBUY_CLIENT = "ezbuy-client";
    }

    public static final class Field {
        public static final String STATE = "state";
        public static final String ORGANIZATION_UNIT_ID = "organizationUnitId";
        public static final String ORGANIZATION_ID = "organizationId";
        public static final String PARENT_ID = "parent_id";
    }

    public interface ExtraParams {
        String SERVICE = "service";
        String HASHED_PASSWORD_TYPE = "hash_password_type";
    }

    public static final String SERVICE_ALIAS = "HUB_AUTH_INTERNAL";

    public enum SyncState {
        WAIT,
        PUSHED
    }

    public static class TelecomServiceIdString {
        public static final String CA = "7";
        public static final String EASY_BOOK = "208";
        public static final String SINVOICE = "37";
        public static final String VCONTRACT = "101";
        public static final String VBHXH = "151";
    }

    public static class TelecomServiceAlias {
        public static final String MYSIGN = "CA";
        public static final String VCONTRACT = "VCONTRACT";
    }

    public interface ALIAS_RULE {
        List<String> ALIAS_NOT_SYNC_POLICY_FIRST =
                List.of("SINVOICE", "VCONTRACT", "SCONTRACT"); // lstAlias not sync policy for first
    }

    public interface OPTION_SET {
        String OPTION_SET_CODE = "optionSetCode";
        String OPTION_SET_VALUE_CODE = "optionSetValueCode";
    }

    public interface VCONTRACT {
        public static final String IS_NOT_HUMAN = "true";
        public static final String CP_CODE_CM_SYS = "CM_SYS";
        public static final String CANCELLED = "Cancelled";
        public static final String COMPLETED = "Completed";
        public static final String REJECTED = "Rejected";
        public static final String CP_CODE_SME_HUB_SYS = "SME_HUB_SYS";
    }

    public interface TENANT_FILE {
        public static final String PYCXTTT = "PYCXTTT";
    }

    // cau hinh bang sme_setting.setting
    public static final class Setting {
        public static final String FLAG_CALL_CRM = "FLAG_CALL_CRM"; // co ON/OFF goi CRM
        public static final String ON = "ON";
        public static final String OFF = "OFF";
    }

    // state bang sme_setting.setting
    public static final class OrgBusinessInfoState {
        public static final String NEW = "new";
        public static final String UPDATED = "updated";
    }
}
