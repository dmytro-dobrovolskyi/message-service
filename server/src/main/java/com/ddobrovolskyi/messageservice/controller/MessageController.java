package com.ddobrovolskyi.messageservice.controller;

import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private MessageLoggingService messageLoggingService;

    @RequestMapping("/")
    void logMessage() {
        messageLoggingService.logMessage("");
    }
}
