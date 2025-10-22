package com.example.experiment1.service;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;

// terminal command to run the docker container on linux (if other OS remove "sudo")
// sudo docker run \
//   --name neo4j-db \
//   --publish=7474:7474 --publish=7687:7687 \
//   --volume=$HOME/neo4j/data:/data \
//   --env NEO4J_AUTH=neo4j/supersecret \
//   neo4j:latest



public class Neo4jPollService {
    public static void main(String... args) {
        final String dbUri = ("bolt://localhost:7687");
        final String dbUser = ("neo4j");
        final String dbPassword = ("supersecret");

        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            driver.verifyConnectivity();
            System.out.println("Connection established");
            driver.close();
        }
    }

}
