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
package com.reactify.config;

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

/**
 * <p>
 * CipherManager class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class CipherManager {
    private final Cipher rsaCipher;

    /**
     * <p>
     * Constructor for CipherManager.
     * </p>
     *
     * @throws Exception
     *             if any.
     */
    public CipherManager() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        this.rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
    }

    /**
     * <p>
     * encrypt.
     * </p>
     *
     * @param message
     *            a {@link String} object
     * @param publicKeyString
     *            a {@link String} object
     * @return a {@link String} object
     */
    public String encrypt(String message, String publicKeyString) {
        try {
            PublicKey publicKey = stringToPublicKey(publicKeyString);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = rsaCipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.HASHING_PASSWORD_FAULT, ex.getMessage());
        }
    }

    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
