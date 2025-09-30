package com.example.experiment1.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class RedisPollService {
    @Autowired StringRedisTemplate stringRedisTemplate;

    private static final long CACHE_TTL_MINUTES = 30;

    public void storePollMetadata(String pollId, String question) {
        String key = "poll:" + pollId;
        stringRedisTemplate.opsForHash().put(key, "id", pollId);
        stringRedisTemplate.opsForHash().put(key, "question", question);

        stringRedisTemplate.expire(key, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }
    public void setVoteCount(String pollId, String optionCaption, int voteCount) {
        String votesKey = "poll:" + pollId + ":votes";
        stringRedisTemplate.opsForHash().put(votesKey, optionCaption, String.valueOf(voteCount));
        stringRedisTemplate.expire(votesKey, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }
    public Long incrementVoteCount(String pollId, String optionCaption) {
        String votesKey = "poll:" + pollId + ":votes";
        Long newCount = stringRedisTemplate.opsForHash().increment(votesKey, optionCaption, 1);

        stringRedisTemplate.expire(votesKey, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        return newCount;
    }
    public boolean isPollCached(String pollId) {
        String votesKey = "poll:" + pollId + ":votes";
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(votesKey));
    }
    

    public boolean isRedisAvailable() {
        try {
            stringRedisTemplate.getConnectionFactory().getConnection().ping();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
