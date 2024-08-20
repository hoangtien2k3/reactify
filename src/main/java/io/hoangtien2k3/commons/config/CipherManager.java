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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CipherManager {
  private final Cipher rsaCipher;

  public CipherManager() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    this.rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
  }

  // ma hoa password custom
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
