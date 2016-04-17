package com.ddobrovolskyi.messageservice.service;

import com.ddobrovolskyi.messageservice.util.Loggable;

public interface MessageLoggingService extends Loggable {
    <T> void logMessage(String message);
}
