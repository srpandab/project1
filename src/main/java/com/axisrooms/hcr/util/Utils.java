package com.axisrooms.hcr.util;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
@Slf4j
public class Utils {

    public static <T> boolean isPresent(T object) {
        return object != null;
    }

    public static boolean isPresent(CharSequence string) {
        return string != null && !string.toString().trim().isEmpty();
    }

    public static <T> boolean isPresent(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static <K, V> boolean isPresent(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> boolean isNotPresent(T object) {
        return !isPresent(object);
    }

    public static boolean isNotPresent(CharSequence string) {
        return !isPresent(string);
    }

    public static <T> boolean isNotPresent(Collection<T> collection) {
        return !isPresent(collection);
    }

    public static <K, V> boolean isNotPresent(Map<K, V> map) {
        return !isPresent(map);
    }

}