package com.ddobrovolskyi.parser;

public interface Parser {
    <T> T parse(String content, Class<T> type);

    boolean canParse(String content);
}
