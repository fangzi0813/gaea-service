package com.cnuip.gaea.service.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = LogProperties.PREFIX)
@Getter
@Setter
@Component
public class LogProperties {
    public static final String PREFIX = "gaea.log";
    private String logFile = "/usr/local/logs";
    private int maxHistory;
    private String maxFileSize;
}
