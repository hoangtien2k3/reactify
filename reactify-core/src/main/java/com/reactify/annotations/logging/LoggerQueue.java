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
package com.reactify.annotations.logging;

import brave.Span;
import com.reactify.model.logging.LoggerDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.Context;

/**
 * <p>
 * LoggerQueue class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class LoggerQueue {
    private static LoggerQueue mMe = null;
    private ArrayBlockingQueue<LoggerDTO> myQueue = null;
    private static final Object myLock = new Object();

    @Getter
    private int countFalse = 0;

    @Getter
    private int countSuccess = 0;

    /**
     * <p>
     * getInstance.
     * </p>
     *
     * @return a {@link LoggerQueue} object
     */
    public static LoggerQueue getInstance() {
        if (mMe == null) {
            mMe = new LoggerQueue();
        }
        return mMe;
    }

    private LoggerQueue() {
        myQueue = new ArrayBlockingQueue<>(100000) {};
    }

    /**
     * <p>
     * clearQueue.
     * </p>
     */
    public void clearQueue() {
        myQueue.clear();
    }

    /**
     * <p>
     * getQueue.
     * </p>
     *
     * @return a {@link LoggerDTO} object
     */
    public LoggerDTO getQueue() {
        return myQueue.poll();
    }

    /**
     * <p>
     * addQueue.
     * </p>
     *
     * @param task
     *            a {@link LoggerDTO} object
     * @return a boolean
     */
    public boolean addQueue(LoggerDTO task) {
        if (myQueue.add(task)) {
            countSuccess++;
            return true;
        }
        countFalse++;
        return false;
    }

    /**
     * <p>
     * addQueue.
     * </p>
     *
     * @param contextRef
     *            a {@link AtomicReference} object
     * @param newSpan
     *            a {@link Span} object
     * @param service
     *            a {@link String} object
     * @param startTime
     *            a {@link Long} object
     * @param endTime
     *            a {@link Long} object
     * @param result
     *            a {@link String} object
     * @param obj
     *            a {@link Object} object
     * @param logType
     *            a {@link String} object
     * @param actionType
     *            a {@link String} object
     * @param args
     *            an array of {@link Object} objects
     * @param title
     *            a {@link String} object
     */
    public void addQueue(
            AtomicReference<Context> contextRef,
            Span newSpan,
            String service,
            Long startTime,
            Long endTime,
            String result,
            Object obj,
            String logType,
            String actionType,
            Object[] args,
            String title) {
        try {
            if (myQueue.add(new LoggerDTO(
                    contextRef, newSpan, service, startTime, endTime, result, obj, logType, actionType, args, title))) {
                countSuccess++;
                return;
            }
        } catch (Exception ignore) {
        }
        countFalse++;
    }

    /**
     * <p>
     * getRecords.
     * </p>
     *
     * @return a {@link List} object
     */
    public List<LoggerDTO> getRecords() {
        List<LoggerDTO> records = new ArrayList<>();
        if (myQueue != null) {
            myQueue.drainTo(records, 100000);
        }
        return records;
    }

    /**
     * <p>
     * getQueueSize.
     * </p>
     *
     * @return a int
     */
    public int getQueueSize() {
        return myQueue.size();
    }

    /**
     * <p>
     * resetCount.
     * </p>
     */
    public void resetCount() {
        countSuccess = 0;
        countFalse = 0;
    }
}
