package com.example.experiment1.benchmark;

import org.neo4j.driver.*;
import org.openjdk.jmh.annotations.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class Neo4jBenchmark {
    private Driver driver;

    @Setup(Level.Trial)
    public void setup() {
        // Connect to Neo4j running in Docker
        driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password")
        );

        // Insert one node for consistent reads
        try (Session session = driver.session()) {
            session.run("MERGE (u:User {id: $id}) SET u.name = $name",
                    Map.of("id", "1", "name", "Alice"));
        }
    }
    @Benchmark
    public void testNeo4jRead(){
        try (Session session = driver.session()) {
            session.run("MATCH (u:User {id: $id}) RETURN u",
                    Map.of("id", "1")).list();
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        driver.close();
    }

}
