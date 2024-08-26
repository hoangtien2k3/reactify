/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/// *
// * Copyright 2024 author - Hoàng Anh Tiến
// *
// * Permission is hereby granted, free of charge, to any person obtaining a
/// copy
// * of this software and associated documentation files (the "Software"), to
/// deal
// * in the Software without restriction, including without limitation the
/// rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// */
// package io.hoangtien2k3.commons.utils; // package
/// io.hoangtien2k3.commons.utils;
//
// import java.security.cert.X509Certificate;
//
// public class FileUtils {
//
// public static String createHash(String certificate, String file) {
// String folderRootCA = "./RootCA";
// X509Certificate[] certChain =
// X509ExtensionUtil.getCertChainOfCert(certificate, folderRootCA);
// String fontPath = "./font/times.ttf";
// SignPdfFile pdfSig = new SignPdfFile();
// return HashFilePDF.getHashTypeRectangleText(pdfSig, file, certChain,
// fontPath);
// }
//
// public static void createHash(SignPdfFile pdfSig, String certificate, String
// file) {
// String folderRootCA = "./RootCA";
// X509Certificate[] certChain =
// X509ExtensionUtil.getCertChainOfCert(certificate, folderRootCA);
// String fontPath = "./font/times.ttf";
// getHashTypeRectangleText(pdfSig, file, certChain, fontPath);
// }
//
// public static Boolean insertSignaturePdfFile(String signature, String
// destPath, String certificate, String file) {
// SignPdfFile pdfSig = new SignPdfFile();
// createHash(pdfSig, certificate, file);
// TimestampConfig timestampConfig = new TimestampConfig();
// timestampConfig.setUseTimestamp(false);
// return pdfSig.insertSignature(signature, destPath, timestampConfig);
// }
//
// public static void getHashTypeRectangleText(SignPdfFile pdfSig, String
// filePath, X509Certificate[] certChain, String fontPath) {
// float widthRectangle = DisplayConfig.WIDTH_RECTANGLE_DEFAULT;
// float heightRectangle = DisplayConfig.HEIGHT_RECTANGLE_DEFAULT;
// int locateSign = DisplayConfig.LOCATE_SIGN_DEFAULT;
// float marginTopOfRectangle = DisplayConfig.MARGIN_TOP_OF_RECTANGLE_DEFAULT;
// float marginBottomOfRectangle = 420;
// float marginRightOfRectangle =
// DisplayConfig.MARGIN_RIGHT_OF_RECTANGLE_DEFAULT;
// float marginLeftOfRectangle = 310;
// String displayText = DisplayConfig.DISPLAY_TEXT_DEFAULT_EMPTY;
// String formatRectangleText = DisplayConfig.FORMAT_RECTANGLE_TEXT_DEFAULT;
// String contact = CertUtils.getCN(certChain[0]);
// String reason = "Ký số";
// String location = "Hà Nội";
// String dateFormatString = DisplayConfig.DATE_FORMAT_STRING_DEFAULT;
// int sizeFont = DisplayConfig.SIZE_FONT_DEFAULT;
// String ou = DisplayConfig.ORGANIZATION_UNIT_DEFAULT_EMPTY;
// String o = DisplayConfig.ORGANIZATION_DEFAULT_EMPTY;
// DisplayConfig displayConfig =
// DisplayConfig.generateDisplayConfigRectangleText(
// 2, widthRectangle, heightRectangle, locateSign,
// marginTopOfRectangle, marginBottomOfRectangle,
// marginRightOfRectangle, marginLeftOfRectangle,
// displayText, formatRectangleText,
// contact, reason, location,
// dateFormatString, fontPath, sizeFont,
// ou, o);
// String digestAlg = SignPdfAsynchronous.HASH_ALGORITHM_SHA256;
// String cryptAlg = SignPdfAsynchronous.CRYPT_ALGORITHM_RSA;
// pdfSig.createHash(filePath, certChain, digestAlg, cryptAlg, displayConfig);
// }
// }
