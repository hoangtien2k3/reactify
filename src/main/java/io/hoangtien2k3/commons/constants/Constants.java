package io.hoangtien2k3.commons.constants;

import org.slf4j.MDC;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class Constants {
    public static final String NAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]+$";
    public static final List<String> IMAGE_EXTENSION_LIST =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "raw", "psd", "ai", "eps");
    public static final int MAX_FILE_SIZE_MB = 3;
    public static final int EMPLOYEE_CODE_LENGTH = 6;
    public static final String EMPLOYEE_CODE_MIN = "000001";

    public static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA);
    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("authorization", "proxy-authorization");

    // for prevent sonar issues
    public static List<String> getSensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

    public interface SoapHeaderConstant {
        String X_B3_TRACEID = "X-B3-TRACEID";
        String X_B3_TRACEID_VALUE_SOAP = MDC.get("X-B3-TraceId");
        String TYPE_XML_CHARSET_UTF8 = "text/xml; charset=utf-8";
        String TYPE_XML = "text/xml";
        String XYZ = "xyz";
    }

    public interface HeaderType {
        String CONTENT_TYPE = "Content-Type";
        String X_API_KEY = "x-api-key";
    }

    public interface DateTimePattern {
        String DMY = "dd/MM/yyyy";
        String DMY_HMS = "dd/MM/yyyy HH:mm:ss";
        String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        String YYYYMMDD = "yyyy-MM-dd";
    }

    public static final class Activation {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
    }

    public static final class Security {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer";
        public static final String DEFAULT_REGISTRATION_ID = "oidc";
    }

    public static final class TokenProperties {
        public static final String USERNAME = "preferred_username";
        public static final String ID = "sub";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String INDIVIDUAL_ID = "individual_id";
        public static final String ORGANIZATION_ID = "organization_id";
    }

    public static final class KeyCloakError {
        public static final String INVALID_GRANT = "INVALID_GRANT";
        public static final String DISABLED = "DISABLED";
        public static final String INVALID = "INVALID";
    }

    public static final class XmlConst {
        public static final String TAG_OPEN_RETURN = "<return>";
        public static final String TAG_CLOSE_RETURN = "</return>";
        public static final String AND_LT_SEMICOLON = "&lt;";
        public static final String AND_GT_SEMICOLON = "&gt;";
        public static final String LT_CHARACTER = "<";
        public static final String GT_CHARACTER = ">";
    }

    public static final class LoggingTitle {
        public static final String REQUEST = "\n-- REQUEST --\n";
        public static final String REQUEST_HEADER = "\n-- REQUEST HEADER --\n";
        public static final String REQUEST_PARAM = "-- REQUEST PARAM --\n";
        public static final String REQUEST_BODY = "-- REQUEST BODY --\n";
        public static final String RESPONSE = "\n-- RESPONSE --\n";
        public static final String PREFIX = "|>";
        public static final Integer BODY_SIZE_REQUEST_MAX = 1000;
        public static final Integer BODY_SIZE_RESPONSE_MAX = 1000;
    }

    public static final class Otp {
        public static final String REGISTER = "REGISTER";
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final String FORGOT_PASSWORD_CONTENT = "OTP for forgot password user";
        public static final String REGISTER_CONTENT = "OTP for register user";
        public static final Integer EXP_MINUTE = 2;
        public static final Integer EXP_OTP_AM_MINUTE = 5;
    }

    public static final class RoleName {
        public static final String SYSTEM = "system";
        public static final String ADMIN = "admin";
        public static final String USER = "user";
    }

    public static final class Sorting {
        public static final String SPLIT_OPERATOR = ",";
        public static final String MINUS_OPERATOR = "-";
        public static final String PLUS_OPERATOR = "+";
        public static final String DESC = "desc";
        public static final String ASC = "asc";
        public static final String FILED_DISPLAY = "$1_$2";
    }

    public static final class ArrayLimit {
        public static final Integer COMMON = 100;
    }

    public static class TemplateMail {
        public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
        public static final String SIGN_UP = "SIGN_UP";
        public static final String CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS";
        public static final String CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS";
        public static final String EMPLOYEE_REGISTER_SUCCESS = "EMPLOYEE_REGISTER_SUCCESS";
        public static final String ACCOUNT_ACTIVE = "ACCOUNT_ACTIVE";
        public static final String SIGN_UP_PASSWORD = "SIGN_UP_PASSWORD";
        public static final String VERIFY_ACCOUNT_SUCESS = "VERIFY_ACCOUNT_SUCESS";
        public static final String NOTI_VERIFY_ACCOUNT = "NOTI_VERIFY_ACCOUNT";
    }

    public static class ActionUser {
        public static final String SYSTEM = "system";
    }

    public interface MINIO_BUCKET_MARKET_INFO {
        String URL_IMAGE = "market-info";
    }

    public interface MINIO_BUCKET_MARKET_SECTION {
        String MARKET_SECTION = "market-section";
    }

    public static final String NULL_IMAGE_SRC = null;

    public static final class COMMON {
        public static final Integer STATUS_ACTIVE = 1;
        public static final Integer STATUS_INACTIVE = 0;
        public static final Integer STATUS_INVALID = -1;
        public static final Integer STATUS_NULL = -2;
        public static final String STR_STATUS_INACTIVE = "0";
    }

    public static class ORGANIZATION_STATUS {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
    }

    public static class STATUS {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
        public static final Integer DELETE = -1;
    }

    public static class STATE {
        public static final Integer ACTIVE = 1;
        public static final Integer INACTIVE = 0;
        public static final Integer DELETE = 3;
    }

    public static class IDENTIFY {
        public static final String MST = "MST";
        public static final String GPKD = "DKKD";
        public static final Integer DELETE = 3;
    }

    public static final class PERMISSION_TYPE {
        public static final String ROLE = "ROLE";
        public static final String GROUP = "GROUP";
    }

    public static final HashSet<String> EXCLUDE_LOGGING_ENDPOINTS = new HashSet<>(List.of("/actuator/health"));

    public static final int MAX_BYTE = 4096;

    public static class POOL {
        public static final String REST_CLIENT_POLL = "Rest-client-Pool"; // name of Rest client poll for https proxy
    }

    public static final class TEMPLATE_PATH {
        public static final String ADD_EMPLOYEE_TEMPLATE = "/template/employee/ADD_EMPLOYEE_TEMPLATE.xlsx";
        public static final String ADD_ROLE_TEMPLATE = "/template/employee/ADD_ROLE_TEMPLATE.xlsx";
        public static final String ADD_UNIT_TEMPLATE = "/template/employee/ADD_UNIT_ORGANIZATION_TEMPLATE.xlsx";

        public static final String ADD_EMPLOYEE_TEMPLATE_RESULT =
                "/template/employee/ADD_EMPLOYEE_TEMPLATE_RESULT.xlsx";
        public static final String ADD_ROLE_TEMPLATE_RESULT = "/template/employee/ADD_ROLE_TEMPLATE_RESULT.xlsx";
    }

    public static final class TEMPLATE_FILE_NAME {
        public static final String ADD_EMPLOYEE_FILE_NAME = "ADD_EMPLOYEE_TEMPLATE.xlsx";
        public static final String ADD_ROLE_FILE_NAME = "ADD_ROLE_TEMPLATE.xlsx";
        public static final String ADD_UNIT_FILE_NAME = "ADD_UNIT_TEMPLATE.xlsx";
        public static final String ADD_EMPLOYEE_RESULT_FILE_NAME = "ADD_EMPLOYEE_TEMPLATE_RESULT.xlsx";
        public static final String ADD_ROLE_RESULT_FILE_NAME = "ADD_ROLE_TEMPLATE_RESULT.xlsx";
    }

    public static final class Role {
        public static final String ENTERPRISE_STAFF = "ENTERPRISE_STAFF";
        public static final String ENTERPRISE_ADMIN = "ENTERPRISE_ADMIN";
        public static final String ENTERPRISE_ADMIN_NEW = "admin";
        public static final String ENTERPRISE_STAFF_NEW = "staff";
        public static final String ENTERPRISE_USER_NEW = "user";
        public static final String SHORT_ENTERPRISE_STAFF = "S";
        public static final String SHORT_ENTERPRISE_ADMIN = "A";
        public static final String SHORT_ENTERPRISE_USER = "U";
    }

    public static final class Gender {
        public static final String SHORT_MALE = "M";
        public static final String SHORT_FEMALE = "F";
        public static final String FULL_MALE = "male";
        public static final String FULL_FEMALE = "female";
    }

    public static final class ROW_TEMPLAE_NAME {
        public static final String FIRST_NAME = "first.name";
        public static final String LAST_NAME = "last.name";
        public static final String FULL_MALE = "full.name";
        public static final String PHONE = "contact.phone";
        public static final String EMAIL = "contact.email";
        public static final String DATE_OF_BIRTH = "individual.date.of.birth";
        public static final String SEX = "gender";
        public static final String WORKING_STATUS = "working.status.row.name";
        public static final String ADDRESS = "employee.address";
        public static final String USERNAME = "login.account";
        public static final String EMPLOYEE_CODE = "employee.code";
        public static final String ACCOUNT_EMAIL = "email.account";
        public static final String CODE = "employee.code";
        public static final String SEX_DESCRIPTION = "sex.des";

        public static final String ORGANIZATION = "organization";
        public static final String POSITION = "working.position";
        public static final String DIRECT_MANAGER = "direct.manage";
        public static final String ONBOARD = "intern.day.template";
        public static final String ONBOARD_DAY = "start.working.day.template";
        public static final String APPLICATION = "application.chose";
        public static final String ROLE_GROUP = "role.group";
        public static final String ROLE = "role.row.des";
        public static final String OBLIGATORY = "(*)";
    }

    public static final class OptionSetCode {
        public static final String SUB_TITLE_MAIL = "SUB_TITLE_MAIL"; // Cau hinh thong tin truyen vao mail
    }
}
