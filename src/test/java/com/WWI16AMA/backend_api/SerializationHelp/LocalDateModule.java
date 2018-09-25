package com.WWI16AMA.backend_api.SerializationHelp;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

public class LocalDateModule extends SimpleModule {
    private static final String NAME = "CustomIntervalModule";
    private static final VersionUtil VERSION_UTIL = new VersionUtil() {
    };

    public LocalDateModule() {
        super(NAME, VERSION_UTIL.version());
        addSerializer(LocalDate.class, new LocalDateSerializer());
    }
}
