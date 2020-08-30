package com.danpopescu.taskmanagement.web;

import org.springframework.http.MediaType;

public class PatchMediaType {

    public static final String APPLICATION_JSON_PATCH_VALUE = "application/json-patch+json";

    public static final String APPLICATION_MERGE_PATCH_VALUE = "application/merge-patch+json";

    public static final MediaType APPLICATION_JSON_PATCH = MediaType.valueOf(APPLICATION_JSON_PATCH_VALUE);

    public static final MediaType APPLICATION_MERGE_PATCH = MediaType.valueOf(APPLICATION_MERGE_PATCH_VALUE);

    private PatchMediaType() {
    }
}
