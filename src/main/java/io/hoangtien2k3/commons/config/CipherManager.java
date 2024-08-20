/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
package io.hoangtien2k3.commons.config;

import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

/**
 * {@code CipherManager} provides functionality for encrypting messages using
 * RSA encryption.
 * <p>
 * This component initializes an RSA {@link Cipher} with OAEP padding and uses
 * it to encrypt messages. It also includes utility methods for converting a
 * Base64 encoded public key string into a {@link PublicKey} instance.
 * </p>
 *
 * <p>
 * The RSA encryption uses the BouncyCastle provider for cryptographic
 * operations.
 * </p>
 */
@Component
public class CipherManager {
    private final Cipher rsaCipher;

    /**
     * Initializes the {@code CipherManager} by setting up the RSA cipher with the
     * BouncyCastle provider.
     *
     * @throws Exception
     *             if an error occurs while initializing the cipher or adding the
     *             security provider
     */
    public CipherManager() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        this.rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
    }

    /**
     * Encrypts a given message using RSA encryption.
     *
     * <p>
     * This method converts the provided public key string into a {@link PublicKey},
     * initializes the RSA cipher for encryption, and then encrypts the message. The
     * encrypted result is encoded in Base64 format.
     * </p>
     *
     * @param message
     *            the message to be encrypted
     * @param publicKeyString
     *            the Base64 encoded public key string
     * @return the Base64 encoded encrypted message
     * @throws BusinessException
     *             if an error occurs during encryption or key conversion
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

    /**
     * Converts a Base64 encoded public key string into a {@link PublicKey}
     * instance.
     *
     * <p>
     * This method decodes the Base64 encoded key string and generates a
     * {@link PublicKey} using the RSA algorithm.
     * </p>
     *
     * @param publicKeyString
     *            the Base64 encoded public key string
     * @return the {@link PublicKey} instance
     * @throws Exception
     *             if an error occurs while decoding the key or generating the
     *             {@link PublicKey}
     */
    private static PublicKey stringToPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
