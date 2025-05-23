package com.uniovi.userservice.repository;


import entities.Score;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends MongoRepository<Score, String> {
    @Query("{'email':  ?0}")
    List<Score> findBestByEmail(Sort sort, String user);
}
