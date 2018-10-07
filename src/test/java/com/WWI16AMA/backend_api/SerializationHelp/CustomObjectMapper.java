package com.WWI16AMA.backend_api.SerializationHelp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        registerModule(new LocalDateModule());
        enable(SerializationFeature.INDENT_OUTPUT);
    }
}