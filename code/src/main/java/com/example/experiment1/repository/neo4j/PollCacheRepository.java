package com.example.experiment1.repository.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.experiment1.domain.neo4j.PollAggregate;

@Repository
public interface PollCacheRepository extends Neo4jRepository<PollAggregate, Long> {

    @Query("""
                MATCH (a:PollAggregate {pollId: $aggregateId})
            DETACH DELETE a
                """)
void deleteAggregateWithOptions(@Param("aggregateId") Long aggregateId);


}
