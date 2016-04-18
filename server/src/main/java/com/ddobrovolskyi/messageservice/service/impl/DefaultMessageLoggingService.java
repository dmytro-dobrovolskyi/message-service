package com.ddobrovolskyi.messageservice.service.impl;

import com.ddobrovolskyi.messageservice.exception.DataTypeNotSupportedException;
import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import com.ddobrovolskyi.parser.Parser;

import java.util.List;

public abstract class DefaultMessageLoggingService implements MessageLoggingService {
    private long messageCount;
    private long totalLength;

    protected DefaultMessageLoggingService() {

    }

    @Override
    public void logMessage(String message, Class<?> messageType) {
        Parser messageParser = getParsers().stream()
                .filter(parser -> parser.canParse(message))
                .findFirst()
                .orElseThrow(DataTypeNotSupportedException::new);

        String processedMessage = messageParser.parse(message, messageType).toString();

        logger().info("=======================================");
        logger().info(processedMessage);
        logger().info("=======================================");
        logger().info(String.format("Processed messages: %d    Total length: %d",
                ++messageCount,
                totalLength += processedMessage.length()
        ));
        logger().info("=======================================");
    }

    /**
     * Gets certain {@code Parser} implementations which are necessary for message conversion.
     *
     * @return certain {@code Parser} implementations
     */
    protected abstract List<? extends Parser> getParsers();
}
