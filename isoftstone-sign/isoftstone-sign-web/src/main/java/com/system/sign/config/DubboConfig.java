package com.system.sign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/context.properties")
@ImportResource({"classpath:config/*.xml"})
public class DubboConfig {

}
