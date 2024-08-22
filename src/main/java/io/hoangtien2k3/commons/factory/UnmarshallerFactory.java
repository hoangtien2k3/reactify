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
package io.hoangtien2k3.commons.factory;

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
