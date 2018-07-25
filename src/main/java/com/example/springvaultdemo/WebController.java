package com.example.springvaultdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Value("${vaultSecret:#{null}}")
    private String vaultSecret;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/")
    public String index(){
        return "Application-name: " +  appName + ", Vault-secret: " + vaultSecret;
    }

}