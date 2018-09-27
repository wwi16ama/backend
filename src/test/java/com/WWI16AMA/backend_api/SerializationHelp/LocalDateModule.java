package com.WWI16AMA.backend_api.SerializationHelp;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

class LocalDateModule extends SimpleModule {
    LocalDateModule() {
        super("LocalDateModule");
        addSerializer(LocalDate.class, new LocalDateSerializer());
    }
}
