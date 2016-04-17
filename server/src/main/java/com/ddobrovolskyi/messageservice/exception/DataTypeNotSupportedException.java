package com.ddobrovolskyi.messageservice.exception;

public class DataTypeNotSupportedException extends RuntimeException {
    public DataTypeNotSupportedException() {
        super("Provided data type is not supported by the system");
    }
}
