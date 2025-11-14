package com.example.experiment1;

import com.example.experiment1.domain.Role;
import com.example.experiment1.domain.jpa.User;
import com.example.experiment1.repository.jpa.UserRepository;

import org.springframework.context.annotation.Bean;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Experiment1Application {
    public static void main(String[] args) {
        SpringApplication.run(Experiment1Application.class, args);


        // use port 32806 for the cache in neo4j desktop
        // CORRECTION: the port changes every time you start the cache, run docker ps to see the port mapping, i.e. something like 32794-7474/tcp, where 32794 is the port to use 
    }
  @Bean
  public CommandLineRunner startupActions(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      String adminUsername = "admin";
      if (userRepository.findByUsername(adminUsername).isEmpty()) {
        User admin = new User(adminUsername, "admin@company.com");
        admin.setPassword(passwordEncoder.encode("password")); // Set a default password
        admin.setRoles(Set.of(Role.ADMIN));
        userRepository.save(admin);
        System.out.println("Default admin user created with username='" + adminUsername + "' and password='password'");
      }
    };
   }
}
