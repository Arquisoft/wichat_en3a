package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    @Query(value = "{ 'language': ?0 }")
    List<Answer> findAnswersByLanguage(String language);

}
