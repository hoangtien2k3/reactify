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
package io.hoangtien2k3.utils;

import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

/**
 * Utility class for logging request and response data buffers. Provides methods
 * to log the content of DataBuffer objects. Supports various media types for
 * logging.
 *
 * @since 20/07/2024
 */
public class LogUtils {

    /**
     * Logs the content of a request DataBuffer.
     *
     * @param log
     *            the Logger to use for logging
     * @param buffer
     *            the DataBuffer containing the request data
     * @param <T>
     *            the type of DataBuffer
     * @return the original DataBuffer wrapped in a new DataBuffer
     */
    @SuppressWarnings("unchecked")
    public static <T extends DataBuffer> T loggingRequest(Logger log, T buffer) {
        return logging(log, "request: ", buffer);
    }

    /**
     * Logs the content of a response DataBuffer.
     *
     * @param log
     *            the Logger to use for logging
     * @param buffer
     *            the DataBuffer containing the response data
     * @param <T>
     *            the type of DataBuffer
     * @return the original DataBuffer wrapped in a new DataBuffer
     */
    public static <T extends DataBuffer> T loggingResponse(Logger log, T buffer) {
        return logging(log, "response: ", buffer);
    }

    /**
     * Logs the content of a DataBuffer with a specified prefix.
     *
     * @param log
     *            the Logger to use for logging
     * @param inOrOut
     *            the prefix to indicate whether it's a request or response
     * @param buffer
     *            the DataBuffer containing the data
     * @param <T>
     *            the type of DataBuffer
     * @return the original DataBuffer wrapped in a new DataBuffer
     */
    private static <T extends DataBuffer> T logging(Logger log, String inOrOut, T buffer) {
        InputStream dataBuffer = buffer.asInputStream();
        byte[] bytes = toByteArray(dataBuffer);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        log.info("{}: {}", inOrOut, new String(bytes));
        DataBufferUtils.release(buffer);
        return (T) nettyDataBufferFactory.wrap(bytes);
    }

    /**
     * Converts an InputStream to a byte array.
     *
     * @param inStream
     *            the InputStream to be converted
     * @return a byte array containing the data from the InputStream
     */
    private static byte[] toByteArray(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        byte[] in_b = new byte[] {};
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in_b;
    }
}
