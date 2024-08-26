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
package io.hoangtien2k3.commons.annotations.logging;

import brave.Span;
import io.hoangtien2k3.commons.model.logging.LoggerDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.Context;

/**
 * LoggerQueue is a singleton class that manages a bounded blocking queue for
 * logging operations. It is designed to handle high-throughput logging tasks by
 * storing logs in a queue and providing various methods to interact with the
 * queue.
 */
@Slf4j
public class LoggerQueue {

    /** Singleton instance of LoggerQueue */
    private static LoggerQueue mMe = null;

    /** Queue to store LoggerDTO objects, with a capacity of 100,000 */
    private ArrayBlockingQueue<LoggerDTO> myQueue = null;

    /** Lock object used for synchronization (currently not in use) */
    private static final Object myLock = new Object();

    /** Counter for failed attempts to add logs to the queue */
    @Getter
    private int countFalse = 0;

    /** Counter for successful attempts to add logs to the queue */
    @Getter
    private int countSuccess = 0;

    /**
     * Returns the singleton instance of LoggerQueue. If the instance is null, a new
     * one is created.
     *
     * @return the singleton instance of LoggerQueue
     */
    public static LoggerQueue getInstance() {
        if (mMe == null) {
            mMe = new LoggerQueue();
        }
        return mMe;
    }

    /**
     * Private constructor to initialize the queue with a fixed capacity of 100,000.
     * This ensures the class follows the singleton pattern.
     */
    private LoggerQueue() {
        myQueue = new ArrayBlockingQueue<>(100000) {};
    }

    /**
     * Clears all elements from the queue.
     */
    public void clearQueue() {
        myQueue.clear();
    }

    /**
     * Retrieves and removes the head of the queue, or returns null if the queue is
     * empty.
     *
     * @return the head of the queue, or null if the queue is empty
     */
    public LoggerDTO getQueue() {
        return myQueue.poll();
    }

    /**
     * Adds a LoggerDTO task to the queue. Increments countSuccess if successful,
     * otherwise increments countFalse.
     *
     * @param task
     *            the LoggerDTO task to add to the queue
     * @return true if the task was successfully added, false otherwise
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
     * Adds a LoggerDTO task to the queue with detailed parameters. This method
     * wraps the parameters into a LoggerDTO object and adds it to the queue.
     *
     * @param contextRef
     *            the Reactor context reference
     * @param newSpan
     *            the span from Sleuth tracing
     * @param service
     *            the service name
     * @param startTime
     *            the start time of the operation
     * @param endTime
     *            the end time of the operation
     * @param result
     *            the result of the operation ("0" for success, "1" for failure)
     * @param obj
     *            additional information about the result
     * @param logType
     *            the type of log
     * @param actionType
     *            the type of action being logged
     * @param args
     *            the method arguments
     * @param title
     *            a custom title for the log entry
     * @return true if the task was successfully added, false otherwise
     */
    public boolean addQueue(
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
                return true;
            }
        } catch (Exception ex) {
            log.error("Failed to add log to the queue", ex);
        }
        countFalse++;
        return false;
    }

    /**
     * Retrieves a list of up to 100,000 LoggerDTO objects from the queue. The queue
     * is drained to the provided list.
     *
     * @return a list of LoggerDTO objects
     */
    public List<LoggerDTO> getRecords() {
        List<LoggerDTO> records = new ArrayList<>();
        if (myQueue != null) {
            myQueue.drainTo(records, 100000);
        }
        return records;
    }

    /**
     * Returns the current size of the queue.
     *
     * @return the number of elements in the queue
     */
    public int getQueueSize() {
        return myQueue.size();
    }

    /**
     * Resets the counters for successful and failed attempts to add logs to the
     * queue.
     */
    public void resetCount() {
        countSuccess = 0;
        countFalse = 0;
    }
}
