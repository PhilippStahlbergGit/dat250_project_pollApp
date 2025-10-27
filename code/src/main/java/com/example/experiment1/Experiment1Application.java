
package com.example.experiment1;

import com.example.experiment1.domain.Role;
import com.example.experiment1.domain.User;
import com.example.experiment1.security.UserRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Experiment1Application {

  public static void main(String[] args) {
    SpringApplication.run(Experiment1Application.class, args);
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
