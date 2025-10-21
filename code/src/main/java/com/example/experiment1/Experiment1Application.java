package com.example.experiment1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class Experiment1Application {
    public static void main(String[] args) {
        SpringApplication.run(Experiment1Application.class, args);
    }
    
}


