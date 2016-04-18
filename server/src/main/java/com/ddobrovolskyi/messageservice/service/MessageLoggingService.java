package com.ddobrovolskyi.messageservice.service;

import com.ddobrovolskyi.messageservice.util.Loggable;

public interface MessageLoggingService extends Loggable {
    /**
     *
     * @param message
     * @param messageType
     * @throws com.ddobrovolskyi.messageservice.exception.DataTypeNotSupportedException
     */
    void logMessage(String message, Class<?> messageType);
}
