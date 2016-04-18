package com.ddobrovolskyi.messageservice.conifg;

import com.ddobrovolskyi.messageservice.conifg.parser.ParserLoader;
import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import com.ddobrovolskyi.messageservice.service.impl.DefaultMessageLoggingService;
import com.ddobrovolskyi.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({ParserProperties.class})
public class ApplicationConfig {
    @Autowired
    private ParserLoader parserLoader;

    ApplicationConfig() {

    }

    @Bean
    public MessageLoggingService messageLoggingService() {
        return new DefaultMessageLoggingService() {
            @Override
            protected List<? extends Parser> getParsers() {
                return parserLoader.loadParsers();
            }
        };
    }
}
