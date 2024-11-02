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
package com.reactify.factory;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * UnmarshallerFactory class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class UnmarshallerFactory {

    private static final Map<Class<?>, Unmarshaller> instance = new HashMap<>();

    /**
     * <p>
     * Getter for the field <code>instance</code>.
     * </p>
     *
     * @param clz
     *            a {@link Class} object
     * @return a {@link Unmarshaller} object
     */
    public static Unmarshaller getInstance(Class<?> clz) {
        Unmarshaller obj = instance.get(clz);
        if (obj != null) return obj;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            instance.put(clz, unmarshaller);
            return unmarshaller;
        } catch (JAXBException e) {
            log.error("Init Unmarshaller error", e);
            return null;
        }
    }
}
