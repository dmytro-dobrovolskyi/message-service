package com.ddobrovolskyi.messageservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Audit {
    private String type;
    private String date;
    private String operation;
}
