package com.ddobrovolskyi.parser.impl;

import com.ddobrovolskyi.parser.Parser;
import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class XmlParser implements Parser {
    private final ObjectMapper objectMapper = new XmlMapper();

    @SneakyThrows(IOException.class)
    public <T> T parse(String content, Class<T> type) {
        return objectMapper.readValue(content, type);
    }

    @SneakyThrows(IOException.class)
    public boolean canParse(String content) {
        return new DataFormatDetector(new XmlFactory()).findFormat(content.getBytes()).hasMatch();
    }
}
