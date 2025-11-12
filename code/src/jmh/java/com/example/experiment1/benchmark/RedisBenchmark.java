package com.example.experiment1.benchmark;
import org.openjdk.jmh.annotations.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 10, time = 2)
@Fork(1)
@State(Scope.Benchmark)
public class RedisBenchmark {
    private RedisTemplate<String, String> redisTemplate;
    private LettuceConnectionFactory connectionFactory;

    @Param({"1","10","100","1000","10000"})
    private int recordCount;

    @Setup(Level.Trial)
    public void setup() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory("localhost", 6379);
        connectionFactory.afterPropertiesSet();
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        for (int i = 0; i < recordCount; i++) {
            redisTemplate.opsForValue().set(
                    "user:" + i,
                    "{\"id\":" + i + ",\"name\":\"Alice" + i + "\"}"
            );
        }

    }

    @Benchmark
    public void testRedis() {
        redisTemplate.opsForValue().get("user:" + recordCount);
    }
    @TearDown(Level.Trial)
    public void tearDown() {
        if (connectionFactory != null) {
            connectionFactory.destroy();
        }

    }
}
