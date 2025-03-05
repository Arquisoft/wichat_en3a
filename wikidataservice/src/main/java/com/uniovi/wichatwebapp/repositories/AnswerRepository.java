package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository  extends MongoRepository<Answer, String> {
    @Query(value = "SELECT MAX(id) AS id, text, language FROM answers WHERE language = ?2 AND text <> ?3 GROUP BY text, language ORDER BY RANDOM() LIMIT ?4", nativeQuery = true)
    List<Answer> findWrongAnswers( String lang, String answerTest, int wrongAnswers);


}
