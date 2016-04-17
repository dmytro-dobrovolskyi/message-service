package com.ddobrovolskyi.messageservice.conifg;

import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import com.ddobrovolskyi.messageservice.service.impl.DefaultMessageLoggingService;
import com.ddobrovolskyi.parser.Parser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties({ParserProperties.class})
public class ApplicationConfig {
    ApplicationConfig() {

    }

    @Bean
    public MessageLoggingService messageLoggingService() {
        return new DefaultMessageLoggingService() {
            @Override
            protected List<? extends Parser> getParsers() {
                return parserLoader().loadParsers();
            }
        };
    }

    @Bean
    public ParserLoader parserLoader() {
        return new ParserLoader();
    }
}

class ParserLoader {
    private static final String JAR_EXT = ".jar";
    private static final String CLASS_EXT = ".class";

    private final List<Path> alreadyScannedJars = new ArrayList<>();
    private final List<Parser> parsers = new ArrayList<>();

    @Autowired
    private ParserProperties parserProperties;

    ParserLoader() {

    }

    @SneakyThrows(IOException.class)
    List<? extends Parser> loadParsers() {
        try (Stream<Path> parserDir = Files.list(Paths.get(parserProperties.getClasspath()))) {
            List<Parser> newParsers = parserDir
                    .filter(path -> path.toString().endsWith(JAR_EXT))
                    .filter(jarPath -> !alreadyScannedJars.contains(jarPath))
                    .peek(alreadyScannedJars::add)
                    .map(Path::toFile)
                    .flatMap(this::loadParsersFromJar)
                    .collect(Collectors.toList());

            parsers.addAll(newParsers);
        }
        return parsers;
    }

    @SneakyThrows(IOException.class)
    private Stream<? extends Parser> loadParsersFromJar(File jar) {
        return new JarFile(jar).stream()
                .map(JarEntry::getName)
                .filter(jarEntry -> jarEntry.endsWith(CLASS_EXT))
                .map(className -> StringUtils.substringBefore(className, CLASS_EXT))
                .map(this::normalizeClassName)
                .map(className -> loadClassByName(className, jar))
                .filter(Parser.class::isAssignableFrom)
                .map(this::instantiate);
    }

    private String normalizeClassName(String pathToClass) {
        return pathToClass.replace('/', '.');
    }

    @SneakyThrows({MalformedURLException.class, ClassNotFoundException.class})
    private Class<?> loadClassByName(String className, File jar) {
        ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + jar + " !/")});
        return classLoader.loadClass(className);
    }

    @SneakyThrows({IllegalAccessException.class, InstantiationException.class, NoSuchMethodException.class})
    private Parser instantiate(Class<?> clazz) {
        ReflectionUtils.makeAccessible(clazz.getDeclaredConstructor());
        return ((Parser) clazz.newInstance());
    }
}
