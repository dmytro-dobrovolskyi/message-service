package com.ddobrovolskyi.messageservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility interface to avoid boilerplate Logger filed initialization code.
 */
public interface Loggable {
    /**
     * Returns singleton {@code Logger} instance.
     *
     * @return certain {@code Logger} instance
     */
    default Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }
}
