package com.rlf.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RlfModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RlfModuleApplication.class,args);
    }
}
