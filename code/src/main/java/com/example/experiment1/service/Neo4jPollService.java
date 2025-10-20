package com.example.experiment1.service;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;

public class Neo4jPollService {
    public static void main(String... args) {
        final String dbUri = ("bolt://localhost:7687");
        final String dbUser = ("neo4j");
        final String dbPassword = ("supersecret");

        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            driver.verifyConnectivity();
            System.out.println("Connection established");
        }
    }

}
