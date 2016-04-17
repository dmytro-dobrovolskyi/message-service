package com.ddobrovolskyi.messageservice.service.impl;

import com.ddobrovolskyi.messageservice.exception.DataTypeNotSupportedException;
import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import com.ddobrovolskyi.parser.Parser;

import java.util.List;

public abstract class DefaultMessageLoggingService implements MessageLoggingService {
    protected DefaultMessageLoggingService() {

    }

    @Override
    public <T> void logMessage(String message) {
        Parser messageParser = getParsers().stream()
                .filter(parser -> parser.canParse(message))
                .findFirst()
                .orElseThrow(DataTypeNotSupportedException::new);

        logger().info(messageParser.parse(message).toString());
    }

    protected abstract List<? extends Parser> getParsers();
}
