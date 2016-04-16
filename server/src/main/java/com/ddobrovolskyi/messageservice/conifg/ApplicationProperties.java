package com.ddobrovolskyi.messageservice.conifg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "parser")
public class ApplicationProperties {

    @Valid
    @NotNull
    private String classpath;
}
