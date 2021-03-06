package com.github.middleware.aggregate.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
public abstract class AsyncUtils {
    private AsyncUtils() {
    }

    public static void awaitDone(AtomicBoolean isComplate, int maxBlockTimeout) {
        final long timeout = TimeUnit.MILLISECONDS.toNanos(maxBlockTimeout);
        final long deadline = System.nanoTime() + timeout;
        while (true) {
            if (isComplate.get() || awaitDone(deadline)) {
                break;
            }
        }
    }

    private static boolean awaitDone(long deadline) {
        long nanos = deadline - System.nanoTime();
        if (nanos <= 0L) {
            return true;
        }
        return false;
    }

    private static boolean awaitDone(long timeout, long deadline) {
        long nanos = deadline - System.nanoTime();
        if (nanos <= 0L) {
            return true;
        }
        return false;
    }
}
