package com.uniovi.wichatwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.uniovi.wichatwebapp.repositories")
public class WichatWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(WichatWebappApplication.class, args);
    }

}
