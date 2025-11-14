package com.example.experiment1.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.experiment1.repository.jpa",
        transactionManagerRef = "jpaTransactionManager"
)
@EnableNeo4jRepositories(
        basePackages = "com.example.experiment1.repository.neo4j",
        transactionManagerRef = "neo4jTransactionManager"
)
public class PersistenceConfig {
    
    @Bean
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public PlatformTransactionManager neo4jTransactionManager(org.neo4j.driver.Driver driver) {
        return new Neo4jTransactionManager(driver);
    }
}

