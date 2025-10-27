package com.example.experiment1.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.experiment1.domain.PollAggregate;

public interface PollCacheRepository extends Neo4jRepository<PollAggregate, String> {

}
