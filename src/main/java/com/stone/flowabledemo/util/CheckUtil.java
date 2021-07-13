package com.stone.flowabledemo.util;

import com.stone.flowabledemo.exception.CheckException;

/**
 * @author Administrator
 */
public class CheckUtil {

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new CheckException(message);
        }
    }
}
