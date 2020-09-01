package com.danpopescu.taskmanagement.web.util;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.domain.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PatchHelperTest {

    PatchHelper patchHelper;
    Task task;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JSR353Module())
                .registerModule(new JavaTimeModule());
        patchHelper = new PatchHelper(objectMapper);

        Project groceries = Project.builder()
                .id(1L)
                .name("Groceries")
                .build();
        task = Task.builder()
                .id(1L)
                .name("Buy milk")
                .project(groceries)
                .dateCreated(LocalDateTime.now())
                .build();
    }

    @Test
    void patch() {
        JsonPatch patch = Json.createPatchBuilder()
                .replace("/name", "Buy juice")
                .remove("/project")
                .build();

        Task patched = patchHelper.patch(patch, task, Task.class);

        assertAll(
                () -> assertEquals("Buy juice", patched.getName()),
                () -> assertNull(patched.getProject())
        );
    }

    @Test
    void mergePatch() {
        JsonMergePatch mergePatch = Json.createMergePatch(Json.createObjectBuilder()
                .add("name", "Buy juice")
                .addNull("project")
                .build());

        Task patched = patchHelper.mergePatch(mergePatch, task, Task.class);

        assertAll(
                () -> assertEquals("Buy juice", patched.getName()),
                () -> assertNull(patched.getProject())
        );
    }
}