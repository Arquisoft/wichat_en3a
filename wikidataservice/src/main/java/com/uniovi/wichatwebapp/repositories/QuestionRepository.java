package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    @Query(value = "{ 'correctAnswer': { $exists: true } }")
    List<Question> findQuestionsByCorrectAnswerIdExists();

    Optional<Question> findById(String id);

}
