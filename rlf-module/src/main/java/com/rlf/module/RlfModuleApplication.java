package com.rlf.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RlfModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RlfModuleApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(1000);
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(3000);
        return new RestTemplate(requestFactory);
    }
}
