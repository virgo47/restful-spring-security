package com.github.virgo47.respsec.main;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = "com.github.virgo47.respsec.main")
@ImportResource("classpath:spring-security.xml")
public class AppConfig {

}
