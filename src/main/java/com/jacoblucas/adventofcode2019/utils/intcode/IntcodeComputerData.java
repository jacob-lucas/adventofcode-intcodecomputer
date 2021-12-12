package com.jacoblucas.adventofcode2019.utils.intcode;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class IntcodeComputerData {
    public static final String INSTRUCTION_POINTER_KEY = "ip";
    public static final String MEMORY_KEY = "mem";
    public static final String RELATIVE_BASE_KEY = "rb";

    private ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    public <T> T get(final String key, final Class<T> type) {
        if (data.containsKey(key)) {
            final Object obj = data.get(key);
            if (obj.getClass().equals(type)) {
                return (T) data.get(key);
            }
        }
        throw new NoSuchElementException(key);
    }

    public <T> T put(final String key, final T value) {
       return (T) data.put(key, value);
    }
}
