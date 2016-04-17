package com.ddobrovolskyi.messageservice.conifg.parser;

import com.ddobrovolskyi.parser.Parser;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParserRegistry {
    private Map<Path, List<Parser>> registry = new HashMap<>();

    ParserRegistry() {

    }

    List<Parser> findByPath(Path path) {
        List<Parser> result = registry.get(path);
        return result == null ? new ArrayList<>() : result;
    }

    void register(Path path, List<Parser> parsers) {
        registry.put(path, parsers);
    }
}
