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
package io.hoangtien2k3.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for handling stream operations.
 * <p>
 * The {@code StreamUtil} class provides a static method for converting an
 * {@link InputStream} into a byte array. This is useful for reading data from a
 * stream and processing it as a byte array.
 * </p>
 *
 * <h2>Class Overview:</h2>
 * <p>
 * This class contains utility methods for working with streams. Specifically,
 * it provides a method to read data from an {@code InputStream} and convert it
 * into a byte array.
 * </p>
 *
 * <h2>Methods:</h2>
 * <ul>
 * <li><strong>streamToByteArray</strong>: Reads all the bytes from an
 * {@code InputStream} and returns them as a byte array.
 * <ul>
 * <li><strong>Parameters:</strong>
 * <ul>
 * <li><code>inStream</code> (<code>InputStream</code>): The input stream to be
 * read.</li>
 * </ul>
 * </li>
 * <li><strong>Returns:</strong>
 * <p>
 * A byte array containing the data read from the input stream. If an error
 * occurs during reading, an empty byte array is returned.
 * </p>
 * </li>
 * <li><strong>Usage:</strong>
 * <p>
 * This method is used to read data from an input stream into a byte array. This
 * is useful when you need to process or manipulate the entire contents of a
 * stream as a byte array.
 * </p>
 * </li>
 * <li><strong>Exceptions:</strong>
 * <p>
 * If an error occurs during the reading process (e.g., an {@link Exception} is
 * thrown), an error message is logged, and an empty byte array is returned.
 * </p>
 * </li>
 * </ul>
 * </li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * InputStream inputStream = new FileInputStream("path/to/file");
 * byte[] data = StreamUtil.streamToByteArray(inputStream);
 * // data now contains the byte array representation of the file
 * }</pre>
 *
 * <h2>Notes:</h2>
 * <p>
 * The buffer size used for reading the stream is 100 bytes. If the stream
 * contains more data, it will be read in chunks until the end of the stream is
 * reached. The method ensures that all data from the input stream is captured
 * in the resulting byte array.
 * </p>
 */
@Slf4j
public class StreamUtil {

    /**
     * Converts an {@link InputStream} into a byte array.
     * <p>
     * This method reads data from the input stream in chunks and writes it to a
     * {@link ByteArrayOutputStream}. Once all data has been read, it is converted
     * into a byte array and returned.
     * </p>
     *
     * @param inStream
     *            The input stream to be read. Must not be <code>null</code>.
     * @return A byte array containing the data read from the input stream. If an
     *         error occurs, an empty byte array is returned.
     */
    public static byte[] streamToByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            log.error("streamToByteArray error ", e);
        }
        return in_b;
    }
}
