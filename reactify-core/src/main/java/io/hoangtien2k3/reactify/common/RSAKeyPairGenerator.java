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
package io.hoangtien2k3.reactify.common;

import java.security.*;
import java.util.Base64;
import lombok.Getter;

/**
 * This is class to gen RSAKeyPair (publicKey and privateKey) <a href=
 * "https://www.devglan.com/java8/rsa-encryption-decryption-java">...</a>
 *
 * @author hoangtien2k3
 */
@Getter
public class RSAKeyPairGenerator {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    /**
     * <p>
     * Constructor for RSAKeyPairGenerator.
     * </p>
     *
     * @throws NoSuchAlgorithmException
     *             if any.
     */
    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        // RSA to indicate Asymmetric Encryption (public-key cryptography)
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        // KeySize: 1024 bit
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    /**
     * <p>
     * main.
     * </p>
     *
     * @param args
     *            an array of {@link String} objects
     * @throws NoSuchAlgorithmException
     *             if any.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        String publicKey = Base64.getEncoder()
                .encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());
        String privateKey = Base64.getEncoder()
                .encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());
        // ham nay dung de sinh ra cap key
        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
    }
}
