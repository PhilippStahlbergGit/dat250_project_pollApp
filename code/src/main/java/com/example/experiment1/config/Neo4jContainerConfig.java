package com.example.experiment1.config;

import org.testcontainers.containers.Neo4jContainer;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jContainerConfig {

    @Bean(destroyMethod = "stop")
    public Neo4jContainer<?> neo4jContainer() {
        Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:latest")
                .withAdminPassword("supersecret");
        neo4jContainer.start();
        return neo4jContainer;
    }

    @Bean
    public Driver neo4jDriver(Neo4jContainer<?> neo4jContainer) {
        return GraphDatabase.driver(neo4jContainer.getBoltUrl(),
                AuthTokens.basic("neo4j", "supersecret"));
    }

}
