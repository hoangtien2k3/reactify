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
package com.reactify.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * <p>
 * PasswordService class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class PasswordService {

    private static PasswordService instance;

    private PasswordService() {}

    /**
     * <p>
     * encrypt.
     * </p>
     *
     * @param plaintext
     *            a {@link String} object
     * @return a {@link String} object
     * @throws Exception
     *             if any.
     */
    public synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1");
        md.update(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] raw = md.digest();
        return new String(Base64.getEncoder().encode(raw));
    }

    /**
     * <p>
     * Getter for the field <code>instance</code>.
     * </p>
     *
     * @return a {@link PasswordService} object
     */
    public static synchronized PasswordService getInstance() {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
}
