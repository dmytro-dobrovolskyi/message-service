package com.ddobrovolskyi.parser.impl;

import com.ddobrovolskyi.parser.Parser;
import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class CsvParser implements Parser {
    private final ObjectMapper objectMapper = new CsvMapper();

    @SneakyThrows(IOException.class)
    public <T> T parse(String content, Class<T> type) {
        return objectMapper.readValue(content, type);
    }

    @SneakyThrows(IOException.class)
    public boolean canParse(String content) {
        return new DataFormatDetector(new CsvFactory()).findFormat(content.getBytes()).hasMatch();
    }
}
