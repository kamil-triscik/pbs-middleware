package com.pbs.middleware.server.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.exception.MergingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("PMD.UselessParentheses")
public class AbstractMerger<T> {

    protected static boolean isSet(Long value) {
        return value != null && value > -1;
    }

    protected static boolean isSet(String value) {
        return isNotBlank(value);
    }

    protected static boolean isSet(List<?> value) {
        return value != null && !value.isEmpty();
    }

    protected static boolean isSet(Map<?, ?> value) {
        return value != null && !value.isEmpty();
    }

    protected static <T> T deepCopy(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper
                    .readValue(objectMapper.writeValueAsString(object), object.getClass());
        } catch (IOException e) {
            throw new MergingException("Merging error");
        }
    }

    protected static Map<String, String> mergeMap(Map<String, String> origin, Map<String, String> change) {
        if (origin == null) {
            return new HashMap<>(change);
        }
        Map<String, String> merged = new HashMap<>(origin);
        merged.entrySet().removeIf(entry -> isBlank(entry.getKey()) || isBlank(entry.getValue()));

        change.entrySet().removeIf(entry -> isBlank(entry.getKey()) || (entry.getValue() != null && entry.getValue().replaceAll(" ", "").isEmpty()));
        change.forEach((key, value) -> {
            if (merged.get(key) != null) {
                if (value != null) {
                    merged.replace(key, value);
                } else {
                    merged.remove(key);
                }
            } else {
                merged.put(key, value);
            }
        });

        return merged;
    }

}
