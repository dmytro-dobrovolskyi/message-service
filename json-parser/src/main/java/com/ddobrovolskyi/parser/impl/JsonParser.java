package com.ddobrovolskyi.parser.impl;

import com.ddobrovolskyi.parser.Parser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class JsonParser implements Parser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows(IOException.class)
    public <T> T parse(String content, Class<T> type) {
        return objectMapper.readValue(content, type);
    }

    @SneakyThrows(IOException.class)
    public boolean canParse(String content) {
        return new DataFormatDetector(new JsonFactory()).findFormat(content.getBytes()).hasMatch();
    }
}
