package com.book.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "vm-config")
@Component
public class VMConfig {

    String origin = "http://localhost:8080/";
    String originMapping = "/**";
    String[] originMethods = new String[]{"POST", "GET", "PUT", "OPTIONS", "DELETE"};

    boolean originCredentials = true;
    String[] originHeaders = new String[]{"*"};

    String[] loginInterceptorIgnoreUri = new String[]{
            "/css", "/js", "/favicon.ico", "/index", "/index.html", "/",
            "/error",
            "/captcha-code.*","/ver-tel.*","/code/check","/tel-code",
            "/emp/password","/reader/password",
            "/root/login/*", "/emp/login/*", "/reader/login/*",
            "/register/.*"
    };

}
