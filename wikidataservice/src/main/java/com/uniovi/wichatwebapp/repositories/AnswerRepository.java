package com.uniovi.wichatwebapp.repositories;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.AnswerCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository  extends MongoRepository<Answer, String> {
    @Query(value = "{ 'language': ?0, 'text': { $ne: ?1 } , 'category': ?2} ")
    List<Answer> findWrongAnswers(String lang, String answerTest, AnswerCategory answerCategory);

    @Query(value = "{ 'language': ?0 }")
    List<Answer> findAnswersByLanguage(String language);

    @Query(value = "{ 'language': ?0, 'category': { $in: ?1 } }")
    List<Answer> findAnswersByLanguageAndQuestionCategory(String language, List<AnswerCategory> categories);

}
