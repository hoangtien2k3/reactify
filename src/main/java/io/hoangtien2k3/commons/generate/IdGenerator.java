package io.hoangtien2k3.commons.generate;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger idCounter = new AtomicInteger();

    public static int generateId() {
        return idCounter.incrementAndGet();
    }
}
