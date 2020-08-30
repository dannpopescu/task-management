package com.danpopescu.taskmanagement.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

@Component
@RequiredArgsConstructor
public class PatchHelper {

    private final ObjectMapper objectMapper;

    public <T> T patch(JsonPatch patch, T bean, Class<T> beanClass) {
        JsonStructure target = objectMapper.convertValue(bean, JsonStructure.class);
        JsonStructure patched = patch.apply(target);
        return objectMapper.convertValue(patched, beanClass);
    }

    public <T> T mergePatch(JsonMergePatch mergePatch, T bean, Class<T> beanClass) {
        JsonValue target = objectMapper.convertValue(bean, JsonValue.class);
        JsonValue patched = mergePatch.apply(target);
        return objectMapper.convertValue(patched, beanClass);
    }
}
