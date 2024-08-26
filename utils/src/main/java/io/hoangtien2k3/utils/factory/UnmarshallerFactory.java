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

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory class for creating and caching {@link Unmarshaller} instances.
 * <p>
 * The {@code UnmarshallerFactory} class provides a method to obtain a singleton
 * {@link Unmarshaller} instance for a given class. It utilizes a cache to
 * ensure that each {@link Unmarshaller} is created only once per class,
 * improving performance and reducing the overhead of creating multiple
 * instances.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class provides a static method to retrieve {@link Unmarshaller}
 * instances, which are cached for efficiency.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>getInstance</strong>: Retrieves or creates an
 * {@link Unmarshaller} instance for the specified class.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>clz</code> (<code>Class</code>): The class for which the
 * {@link Unmarshaller} is to be created or retrieved.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * An {@link Unmarshaller} instance for the specified class, or
 * <code>null</code> if an error occurs during creation.
 * </p>
 * </li>
 * <li><strong>Exceptions:</strong>
 * <ul>
 * <li><code>JAXBException</code>: Thrown if an error occurs during the creation
 * of the {@link Unmarshaller}.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Private Fields:</h2>
 * <ul>
 * <li><strong>instance</strong>: A <code>Map<Class, Unmarshaller></code> that
 * caches {@link Unmarshaller} instances.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * Unmarshaller unmarshaller = UnmarshallerFactory.getInstance(MyClass.class);
 * MyClass myObject = (MyClass) unmarshaller.unmarshal(new StringReader(xmlData));
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * This factory class uses a cache to ensure that {@link Unmarshaller} instances
 * are only created once per class, thus optimizing the performance of XML
 * unmarshalling operations.
 * </p>
 */
@Slf4j
public class UnmarshallerFactory {

    private static Map<Class, Unmarshaller> instance = new HashMap<>();

    /**
     * Retrieves or creates an {@link Unmarshaller} instance for the specified
     * class.
     * <p>
     * If an {@link Unmarshaller} for the given class is already cached, it is
     * returned. Otherwise, a new {@link Unmarshaller} is created and cached for
     * future use.
     * </p>
     *
     * @param clz
     *            The class for which the {@link Unmarshaller} is to be created or
     *            retrieved.
     * @return An {@link Unmarshaller} instance for the specified class, or
     *         <code>null</code> if an error occurs.
     */
    public static Unmarshaller getInstance(Class clz) {
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
