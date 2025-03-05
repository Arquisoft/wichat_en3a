package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    @Query(value = "SELECT q.* FROM questions q INNER JOIN answers a ON q.correct_answer_id=a.id WHERE a.language = ?1 "+
            " ORDER BY RANDOM() LIMIT 1 ", nativeQuery = true)
    Optional<Question> findRandomQuestion(String lang);

    Optional<Question> findById(String id);
}
