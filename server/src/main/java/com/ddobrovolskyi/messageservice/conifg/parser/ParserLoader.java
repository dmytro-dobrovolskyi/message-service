package com.ddobrovolskyi.messageservice.conifg.parser;

import com.ddobrovolskyi.messageservice.conifg.ParserProperties;
import com.ddobrovolskyi.messageservice.util.Loggable;
import com.ddobrovolskyi.parser.Parser;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ParserLoader implements Loggable {
    private static final String JAR_EXT = ".jar";
    private static final String CLASS_EXT = ".class";
    private static final String TMP_FILE_PREFIX = "parser-dependency-";
    private static final String TMP_FILE_SUFFIX = ".tmp";

    @Autowired
    private ParserProperties parserProperties;

    @Autowired
    private ParserRegistry parserRegistry;

    ParserLoader() {

    }

    @PostConstruct
    private void loadOnStartup() {
        loadParsers();
    }

    @SneakyThrows(IOException.class)
    public List<? extends Parser> loadParsers() {
        try (Stream<Path> parserDir = Files.list(Paths.get(parserProperties.getClasspath()))) {
            return parserDir
                    .filter(path -> path.toString().endsWith(JAR_EXT))
                    .flatMap(loadParsersByJarPath())
                    .collect(Collectors.toList());
        }
    }

    private Function<Path, Stream<? extends Parser>> loadParsersByJarPath() {
        return jarPath -> {
            List<Parser> parsers = parserRegistry.findByPath(jarPath);

            if (parsers.isEmpty()) {
                parserRegistry.register(jarPath, parsers);

                return loadParsersFromJar(jarPath.toFile()).peek(parsers::add);
            }
            return parsers.stream();
        };
    }

    @SneakyThrows(IOException.class)
    private Stream<? extends Parser> loadParsersFromJar(File jar) {
        logger().info("Loading " + jar);
        loadDependencies(jar);

        return new JarFile(jar)
                .stream()
                .map(JarEntry::getName)
                .filter(jarEntryName -> jarEntryName.endsWith(CLASS_EXT))
                .map(this::normalizeClassName)
                .map(className -> loadClassByName(className, jar))
                .filter(Parser.class::isAssignableFrom)
                .map(this::instantiateParser);
    }

    @SneakyThrows(IOException.class)
    private void loadDependencies(File jar) {
        JarFile jarFile = new JarFile(jar);

        jarFile.stream().parallel()
                .filter(jarEntry -> jarEntry.getName().endsWith(JAR_EXT))
                .map(dependencyJar -> createFileFromDependencyJar(jarFile, dependencyJar))
                .peek(this::loadJar)
                .forEach(this::loadDependencies);
    }

    @SneakyThrows({IOException.class})
    private File createFileFromDependencyJar(JarFile parentJar, JarEntry dependencyJar) {
        File tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
        tempFile.deleteOnExit();
        try (InputStream in = parentJar.getInputStream(dependencyJar);
             FileOutputStream out = new FileOutputStream(tempFile)
        ) {
            IOUtils.copy(in, out);
            logger().debug(dependencyJar + ": " + tempFile.toString());
            return tempFile;
        }
    }

    @SneakyThrows(MalformedURLException.class)
    private void loadJar(File jar) {
        ClassLoader classLoader = getClass().getClassLoader();
        Method addURLMethod = ReflectionUtils.findMethod(URLClassLoader.class, "addURL", URL.class);
        addURLMethod.setAccessible(true);
        ReflectionUtils.invokeMethod(addURLMethod, classLoader, jar.toURI().toURL());

        logger().debug(jar + " loaded");
    }

    private String normalizeClassName(String className) {
        return StringUtils.substringBefore(className, CLASS_EXT).replace('/', '.');
    }

    @SneakyThrows({MalformedURLException.class, ClassNotFoundException.class})
    private Class<?> loadClassByName(String className, File jar) {
        ClassLoader classLoader = new URLClassLoader(
                new URL[]{new URL("jar:file:" + jar + " !/")},
                getClass().getClassLoader()
        );
        return classLoader.loadClass(className);
    }

    @SneakyThrows({IllegalAccessException.class, InstantiationException.class, NoSuchMethodException.class})
    private Parser instantiateParser(Class<?> clazz) {
        ReflectionUtils.makeAccessible(clazz.getDeclaredConstructor());
        Parser parser = (Parser) clazz.newInstance();
        logger().info(clazz + " instantiated");
        return parser;
    }
}
