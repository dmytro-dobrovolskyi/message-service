package com.ddobrovolskyi.parser;

public interface Parser {
    <T> T parse(String content);

    boolean canParse(String content);
}
