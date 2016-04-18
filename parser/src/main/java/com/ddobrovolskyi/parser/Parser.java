package com.ddobrovolskyi.parser;

/**
 * Generic converter interface for converting raw String data to its Java representation.
 */
public interface Parser {

    /**
     * Converts raw String data to its Java representation.
     *
     * @param content raw String representation
     * @param type    class to which {@literal content} will be converted
     * @param <T>     corresponding type to which {@literal content} will be converted
     * @return {@literal content} {@code T} instance representation
     */
    <T> T parse(String content, Class<T> type);

    /**
     * Tests if provided raw String data can be converted to its Java representation.
     *
     * @param content raw String representation
     * @return {@literal true} if {@literal content} can be converted to its Java representation,
     * false if not
     */
    boolean canParse(String content);
}
