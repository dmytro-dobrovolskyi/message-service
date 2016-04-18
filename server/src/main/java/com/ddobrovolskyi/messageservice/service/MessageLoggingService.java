package com.ddobrovolskyi.messageservice.service;

import com.ddobrovolskyi.messageservice.exception.DataTypeNotSupportedException;
import com.ddobrovolskyi.messageservice.util.Loggable;

/**
 * Service for single format message logging.
 */
public interface MessageLoggingService extends Loggable {
    /**
     * Converts message to its Java representation if such data type is supported and logs message
     * using {@code Loggable} logger implementation.
     *
     * @param message raw message representation
     * @param messageType corresponding Java type to which message will be converted
     * @throws DataTypeNotSupportedException if message data type conversion is not supported by the system
     */
    void logMessage(String message, Class<?> messageType);
}
