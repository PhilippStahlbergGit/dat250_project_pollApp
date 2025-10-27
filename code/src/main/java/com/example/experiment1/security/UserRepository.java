package com.example.experiment1.security;

import org.springframework.data.repository.CrudRepository;
import com.example.experiment1.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByUsername(String username);
}
