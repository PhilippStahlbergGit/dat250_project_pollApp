package com.example.experiment1;

import redis.clients.jedis.UnifiedJedis;

public class RedisDataLoader {
    public static void main(String[] args) {
        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

        try {
            System.out.println("Loading data into Redis");

            String pollId = "1";

            jedis.del("poll:" + pollId);
            jedis.del("poll:" + pollId + ":votes");
            jedis.del("logged_in_users");

            jedis.hset("poll:" + pollId, "id", pollId);
            jedis.hset("poll:" + pollId, "title","Pineapple on pizza?");

            jedis.hset("poll:"+ pollId + ":votes", "Yes, yammy!", "1");
            jedis.hset("poll:"+ pollId + ":votes", "No, yucky!", "269");

            jedis.sadd("logged_in_users","alice","bob","eve");

            System.out.println(jedis.hgetAll("poll:"+pollId+":votes"));

        } finally {
            jedis.close();
        }
    }
}
