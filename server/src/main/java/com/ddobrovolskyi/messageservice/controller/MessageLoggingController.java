package com.ddobrovolskyi.messageservice.controller;

import com.ddobrovolskyi.messageservice.dto.Audit;
import com.ddobrovolskyi.messageservice.service.MessageLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageLoggingController {

    @Autowired
    private MessageLoggingService messageLoggingService;

    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    ResponseEntity<String> logAuditMessage(@RequestBody String message) {
        messageLoggingService.logMessage(message, Audit.class);
        return new ResponseEntity<>("Message logged successfully", HttpStatus.OK);
    }
}
