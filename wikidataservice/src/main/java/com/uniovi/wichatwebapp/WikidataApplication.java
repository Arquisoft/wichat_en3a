package com.uniovi.wichatwebapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WikidataApplication {
    public WikidataApplication() { }

    public static void main(java.lang.String[] args) {
        SpringApplication.run(WikidataApplication.class, args);
    }
}

