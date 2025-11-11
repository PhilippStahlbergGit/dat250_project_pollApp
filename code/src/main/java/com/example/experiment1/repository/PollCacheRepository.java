package com.example.experiment1.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.experiment1.domain.PollAggregate;

@Repository
public interface PollCacheRepository extends Neo4jRepository<PollAggregate, Long> {

    @Query("MATCH (a:PollAggregate)-[r:HAS_OPTION]->(o:OptionVote) " +
       "WHERE a.pollId = $aggregateId " +
       "DETACH DELETE a, o")
void deleteAggregateWithOptions(@Param("aggregateId") Long aggregateId);


}
