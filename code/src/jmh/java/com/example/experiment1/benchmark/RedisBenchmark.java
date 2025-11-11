package com.example.experiment1.benchmark;
import org.openjdk.jmh.annotations.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class RedisBenchmark {
    private RedisTemplate<String, String> redisTemplate;
    private LettuceConnectionFactory connectionFactory;

    @Setup(Level.Trial)
    public void setup() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory("localhost", 6379);
        connectionFactory.afterPropertiesSet();
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        redisTemplate.opsForValue().set("user:1", "{\"id\":1,\"name\":\"Alice\"}");
    }

    @Benchmark
    public void testRedis() {
        redisTemplate.opsForValue().get("user:1");
    }
    @TearDown(Level.Trial)
    public void tearDown() {
        if (connectionFactory != null) {
            connectionFactory.destroy();
        }

    }
}
