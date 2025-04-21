package entities;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(
        collection = "answers"
)
public class Answer{
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String text;
    private String language;
    //@OneToMany(mappedBy = "correctAnswer", fetch = FetchType.EAGER)
    private List<Question> questions;
    private AnswerCategory category;



    //@ManyToMany(mappedBy = "answers", fetch = FetchType.EAGER)
    private List<Question> questionsWithThisAnswer;



    public Answer() {
    }

    public Answer(String text, String langCode){
        this.text = text;
        this.language = langCode;
    }

    public Answer(String text, AnswerCategory answerCategory, String langCode) {

        this.id = new ObjectId().toString(); // Generate a new ObjectId for the id field
        this.text = text;
        this.language = langCode;
        this.category = answerCategory;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public AnswerCategory getCategory() {
        return category;
    }

    public void setCategory(AnswerCategory category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }
}
