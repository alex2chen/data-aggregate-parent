package com.github.middleware.aggregate.util;

import java.lang.reflect.Array;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/30
 */
public abstract class ArrayUtils {
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static Class<?>[] nullToEmpty(Class<?>[] array) {
        return isEmpty((Object[]) array) ? EMPTY_CLASS_ARRAY : array;
    }

    public static boolean isEmpty(Object[] array) {
        return getLength(array) == 0;
    }

    public static int getLength(Object array) {
        return array == null ? 0 : Array.getLength(array);
    }
}
