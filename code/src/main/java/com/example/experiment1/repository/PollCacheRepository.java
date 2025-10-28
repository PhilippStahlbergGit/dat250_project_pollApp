package com.example.experiment1.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.example.experiment1.domain.PollAggregate;

@Repository
public interface PollCacheRepository extends Neo4jRepository<PollAggregate, String> {

}
