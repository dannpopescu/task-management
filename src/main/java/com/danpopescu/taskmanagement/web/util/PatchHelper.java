package com.danpopescu.taskmanagement.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.json.JsonPatch;
import javax.json.JsonStructure;

@Component
@RequiredArgsConstructor
public class PatchHelper {

    private final ObjectMapper objectMapper;

    public <T> T patch(JsonPatch patch, T bean, Class<T> beanClass) {
        JsonStructure target = objectMapper.convertValue(bean, JsonStructure.class);
        JsonStructure patched = patch.apply(target);
        return objectMapper.convertValue(patched, beanClass);
    }
}
