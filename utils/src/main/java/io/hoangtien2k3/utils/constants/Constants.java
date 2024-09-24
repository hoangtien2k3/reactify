/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hoangtien2k3.utils.constants;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;

/**
 * <p>
 * Constants class.
 * </p>
 *
 * @author hoangtien2k3
 */
public final class Constants {
    /**
     * Constant
     * <code>NAME_PATTERN="^[a-zA-Z&agrave;&aacute;&atilde;ạảăắằẳẵ"{trunked}</code>
     */
    public static final String NAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";
    /**
     * Constant
     * <code>EMAIL_PATTERN="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+["{trunked}</code>
     */
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    /** Constant <code>DATE_PATTERN="\\d{2}[/]\\d{2}[/]\\d{4}"</code> */
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";
    /** Constant <code>ID_NO_PATTERN="^[0-9\\-]+$"</code> */
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";
    /** Constant <code>NUMBER_PATTERN="^[0-9]+$"</code> */
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    /** Constant <code>USERNAME_PATTERN="^[A-Za-z0-9_-]+$"</code> */
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]+$";
    /** Constant <code>IMAGE_EXTENSION_LIST</code> */
    public static final List<String> IMAGE_EXTENSION_LIST =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "raw", "psd", "ai", "eps");
    /** Constant <code>MAX_FILE_SIZE_MB=3</code> */
    public static final int MAX_FILE_SIZE_MB = 3;
    /** Constant <code>EMPLOYEE_CODE_LENGTH=6</code> */
    public static final int EMPLOYEE_CODE_LENGTH = 6;
    /** Constant <code>EMPLOYEE_CODE_MIN="000001"</code> */
    public static final String EMPLOYEE_CODE_MIN = "000001";

    /** Constant <code>VISIBLE_TYPES</code> */
    public static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA);

    public static final class XmlConst {
        public static final String TAG_OPEN_RETURN = "<return>";
        public static final String TAG_CLOSE_RETURN = "</return>";
        public static final String AND_LT_SEMICOLON = "&lt;";
        public static final String AND_GT_SEMICOLON = "&gt;";
        public static final String LT_CHARACTER = "<";
        public static final String GT_CHARACTER = ">";
    }

    public static final class Sorting {
        public static final String SPLIT_OPERATOR = ",";
        public static final String MINUS_OPERATOR = "-";
        public static final String PLUS_OPERATOR = "+";
        public static final String DESC = "desc";
        public static final String ASC = "asc";
        public static final String FILED_DISPLAY = "$1_$2";
    }
}
