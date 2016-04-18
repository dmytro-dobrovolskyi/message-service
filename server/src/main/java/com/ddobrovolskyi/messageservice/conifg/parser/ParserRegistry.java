package com.ddobrovolskyi.messageservice.conifg.parser;

import com.ddobrovolskyi.parser.Parser;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds {@code Parser} implementations.
 */
@Component
class ParserRegistry {
    private Map<Path, List<Parser>> registry = new HashMap<>();

    ParserRegistry() {

    }

    /**
     * Searches {@code Parser} implementations by provided {@literal path}.
     *
     * @param path physical file system path of {@code Parser}s jar.
     * @return certain {@code Parser} implementations or empty {@code List} if
     * such implementation cannot be found
     */
    List<Parser> findByPath(Path path) {
        List<Parser> result = registry.get(path);
        return result == null ? new ArrayList<>() : result;
    }

    /**
     * Adds {@code Parser} implementations to registry, which later can be retrieved by
     * provided {@literal path}.
     *
     * @param path    physical file system path of {@code Parser}s jar.
     * @param parsers certain {@code Parser} implementations
     */
    void register(Path path, List<Parser> parsers) {
        registry.put(path, parsers);
    }
}
