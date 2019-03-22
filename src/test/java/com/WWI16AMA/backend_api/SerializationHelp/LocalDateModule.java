package com.WWI16AMA.backend_api.SerializationHelp;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;

class LocalDateModule extends SimpleModule {
    LocalDateModule() {
        super("LocalDateModule");
        addSerializer(LocalDate.class, new LocalDateSerializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    }
}
