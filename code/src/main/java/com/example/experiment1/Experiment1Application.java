package com.example.experiment1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;


@SpringBootApplication
@RestController
public class Experiment1Application {
    public static void main(String[] args) {
        SpringApplication.run(Experiment1Application.class, args);
    }


	@Bean
	public CommandLineRunner startupActions(@AutoWired PollManager pollManager) {
		return args -> {

            String adminID = "admin";
            if(!pollManager.getUsers().containsKey(adminID)){
                User admin = new User("admin", "admin@company.com");
                admin.setUserId(adminID);
                admin.setRoles(Set.Of(Role.ADMIN);
                pollManager.getUsers().put(adminID,admin);
                // print created admin
            }else{
                User existing = pollManager.getUsers().get(adminID);
                if(existing.getRoles() == null || !existing.contains(adminID)){
                        existing.getRoles().add(Role.ADMIN);
                        // print added admin
                }
            }



		};

    }


