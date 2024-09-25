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
package io.hoangtien2k3.utils.factory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * MarshallerFactory class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class MarshallerFactory {
    private static final Map<Class<?>, Marshaller> instance = new HashMap<>();

    /**
     * <p>
     * convertObjectToXML.
     * </p>
     *
     * @param obj
     *            a {@link java.lang.Object} object
     * @param cls
     *            a {@link java.lang.Class} object
     * @return a {@link java.lang.String} object
     */
    public static String convertObjectToXML(Object obj, Class<?> cls) {
        Marshaller marshaller = instance.get(cls);
        String xmlTxt = "";
        try {
            // create an instance of `JAXBContext`
            // create an instance of `Marshaller`
            if (marshaller == null) {
                marshaller = JAXBContext.newInstance(cls).createMarshaller();
                // enable pretty-print XML output
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                instance.put(cls, marshaller);
            }

            // write XML to `StringWriter`
            StringWriter sw = new StringWriter();

            // convert book object to XML
            marshaller.marshal(obj, sw);

            xmlTxt = sw.toString();

        } catch (JAXBException ex) {
            log.error("Convert Object To XML  fail: ", ex);
        }
        return xmlTxt;
    }
}
