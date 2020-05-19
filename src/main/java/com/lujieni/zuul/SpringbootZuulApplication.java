package com.lujieni.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class SpringbootZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootZuulApplication.class, args);
    }

}
