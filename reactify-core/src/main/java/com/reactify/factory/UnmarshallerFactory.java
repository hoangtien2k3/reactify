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

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Factory class for providing instances of {@link Unmarshaller}
 * for specific classes.
 * </p>
 *
 * <p>
 * This class leverages caching to avoid repeated creation of
 * {@link Unmarshaller} instances for the same class, improving
 * performance when unmarshalling XML to Java objects.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>
 * {@code
 * Unmarshaller unmarshaller = UnmarshallerFactory.getInstance(MyClass.class);
 * MyClass obj = (MyClass) unmarshaller.unmarshal(new StringReader(xmlString));
 * }
 *
 * This class does not provide a default constructor as it is designed
 * to be instantiated with specific parameters. The absence of a default
 * constructor ensures that instances of this class are created in a valid state.
 *
 * </pre>
 *
 * @see Unmarshaller
 * @see JAXBContext
 * @see JAXBException
 * @since 1.0
 * @version 1.0
 * @author hoangtien2k3
 */
@Slf4j
public class UnmarshallerFactory {

    /**
     * Constructs a new instance of {@code UnmarshallerFactory}.
     * <p>
     * This default constructor is provided for compatibility purposes and does not
     * perform any initialization.
     * </p>
     */
    public UnmarshallerFactory() {}

    /**
     * Cache to hold {@link Unmarshaller} instances for specific classes.
     */
    private static final Map<Class<?>, Unmarshaller> instance = new HashMap<>();

    /**
     * <p>
     * Provides an {@link Unmarshaller} instance for the specified
     * class. If an {@link Unmarshaller} has already been created for
     * the class, it is retrieved from the cache. Otherwise, a new instance is
     * created and cached.
     * </p>
     *
     * @param clz
     *            the {@link Class} type for which to retrieve an
     *            {@link Unmarshaller}
     * @return an {@link Unmarshaller} instance configured for the
     *         specified class, or {@code null} if an error occurs during
     *         instantiation
     */
    public static Unmarshaller getInstance(Class<?> clz) {
        // Attempt to retrieve a cached unmarshaller for the class
        Unmarshaller obj = instance.get(clz);
        if (obj != null) return obj;

        try {
            // Create a new JAXB context and unmarshaller for the class
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Cache the created unmarshaller
            instance.put(clz, unmarshaller);
            return unmarshaller;
        } catch (JAXBException e) {
            // Log error if unmarshaller creation fails
            log.error("Init Unmarshaller error", e);
            return null;
        }
    }
}
