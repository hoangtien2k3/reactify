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
package com.reactify.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class DataWsUtil {

    /**
     * <p>
     * getDataByTag.
     * </p>
     *
     * @param realData
     *            a {@link java.lang.String} object
     * @param fromKey
     *            a {@link java.lang.String} object
     * @param toKey
     *            a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String getDataByTag(String realData, String fromKey, String toKey) {
        String data = "";
        if (!DataUtil.isNullOrEmpty(fromKey) && !DataUtil.isNullOrEmpty(realData) && realData.contains(fromKey)) {
            data = realData.substring(realData.indexOf(fromKey) + fromKey.length(), realData.indexOf(toKey));
            if (DataUtil.isNullOrEmpty(data) || DataUtil.safeEqual(data, "N/A")) {
                data = "";
            }
        }
        return data;
    }

    /**
     * <p>
     * wrapTag.
     * </p>
     *
     * @param data
     *            a {@link java.lang.String} object
     * @param openTag
     *            a {@link java.lang.String} object
     * @param closeTag
     *            a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String wrapTag(String data, String openTag, String closeTag) {
        if (DataUtil.isNullOrEmpty(openTag) || DataUtil.isNullOrEmpty(closeTag) || DataUtil.isNullOrEmpty(data)) {
            return "";
        }
        return openTag + data + closeTag;
    }

    /**
     * <p>
     * parseXmlFile.
     * </p>
     *
     * @param in
     *            a {@link java.lang.String} object
     * @return a {@link org.w3c.dom.Document} object
     * @throws java.lang.Exception
     *             if any.
     */
    public static Document parseXmlFile(String in) throws Exception {
        try {
            DocumentBuilderFactory dbf = getDocumentBuilderFactory();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * fixSecurityDocumentBuilder.
     * </p>
     *
     * @param dbf
     *            a {@link javax.xml.parsers.DocumentBuilderFactory} object
     * @throws javax.xml.parsers.ParserConfigurationException
     *             if any.
     */
    public static void fixSecurityDocumentBuilder(DocumentBuilderFactory dbf) throws ParserConfigurationException {
        String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
        dbf.setFeature(FEATURE, true);
        // If you can't completely disable DTDs, then at least do the following:
        // Xerces 1 -
        // http://xerces.apache.org/xerces-j/features.html#external-general-entities
        // Xerces 2 -
        // http://xerces.apache.org/xerces2-j/features.html#external-general-entities
        FEATURE = "http://xml.org/sax/features/external-general-entities";
        dbf.setFeature(FEATURE, false);
        // Xerces 1 -
        // http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
        // Xerces 2 -
        // http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
        FEATURE = "http://xml.org/sax/features/external-parameter-entities";
        dbf.setFeature(FEATURE, false);
        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and
        // Entity Attacks"
        // (see reference
        // below)
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
    }

    /**
     * <p>
     * getDocumentBuilderFactory.
     * </p>
     *
     * @return a {@link javax.xml.parsers.DocumentBuilderFactory} object
     * @throws javax.xml.parsers.ParserConfigurationException
     *             if any.
     */
    public static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        fixSecurityDocumentBuilder(dbf);
        dbf.setNamespaceAware(true);
        return dbf;
    }

    /**
     * <p>
     * formatXML.
     * </p>
     *
     * @param unformattedXml
     *            a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute("indent-number", 3);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            transformer.transform(source, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <p>
     * getListDataByTag.
     * </p>
     *
     * @param realData
     *            a {@link java.lang.String} object
     * @param fromKey
     *            a {@link java.lang.String} object
     * @param toKey
     *            a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    public static List<String> getListDataByTag(String realData, String fromKey, String toKey) {
        List<String> list = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(realData)) {
            return list;
        }
        int index;
        while (realData.contains(toKey)) {
            String data = getDataByTag(realData, fromKey, toKey);
            list.add(data);
            index = realData.indexOf(toKey) + toKey.length();
            realData = realData.substring(index);
        }

        return list;
    }
}
