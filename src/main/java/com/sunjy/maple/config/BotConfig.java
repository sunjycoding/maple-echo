package com.sunjy.maple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author created by sunjy on 11/30/23
 */
@Data
@ConfigurationProperties(prefix = "bot")
@Configuration
public class BotConfig {

    private Long qqNumber;

    private String password;

}
