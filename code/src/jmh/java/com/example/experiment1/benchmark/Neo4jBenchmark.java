package com.example.experiment1.benchmark;

import org.neo4j.driver.*;
import org.openjdk.jmh.annotations.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 10, time = 2)
@Fork(1)
@State(Scope.Benchmark)
public class Neo4jBenchmark {
    private Driver driver;

    @Param({"1","10","100","1000","10000"})
    private int recordCount;
    @Setup(Level.Trial)
    public void setup() {
        // Connect to Neo4j running in Docker
        driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "password")
        );

        // Insert one node for consistent reads
        try (Session session = driver.session()) {
            //Clean up
            session.run("MATCH (u:User) DELETE u");

            for (int i = 0; i < recordCount; i++) {
                session.run("MERGE (u:User {id: $id}) SET u.name = $name",
                        Map.of("id", String.valueOf(i), "name", "Alice" + i));
            }

        }
    }
    @Benchmark
    public void testNeo4jRead(){
        try (Session session = driver.session()) {
            session.run("MATCH (u:User {id: $id}) RETURN u",
                    Map.of("id", String.valueOf(recordCount))).list();
        }
    }


    @TearDown(Level.Trial)
    public void tearDown() {
        driver.close();
    }

}
