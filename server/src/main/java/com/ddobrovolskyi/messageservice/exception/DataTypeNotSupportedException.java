package com.ddobrovolskyi.messageservice.exception;

/**
 * Exception thrown when provided raw data type cannot be converted to its Java representation.
 */
public class DataTypeNotSupportedException extends RuntimeException {
    public DataTypeNotSupportedException() {
        super("Provided data type is not supported by the system");
    }
}
