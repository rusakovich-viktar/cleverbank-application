package ru.clevertec.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlywayConfig {
    private String url;
    private String user;
    private String password;
}
