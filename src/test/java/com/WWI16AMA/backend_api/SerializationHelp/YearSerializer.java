package com.WWI16AMA.backend_api.SerializationHelp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Year;

public class YearSerializer extends JsonSerializer<Year> {
    @Override
    public void serialize(Year value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}
