package entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(
        collection = "questions"
)
public class Question {


        @Id
        //@GeneratedValue(strategy = GenerationType.IDENTITY)
        private String id;
        /*
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name="questions_answers",
                joinColumns=
                @JoinColumn(name="question_id", referencedColumnName="id"),
                inverseJoinColumns=
                @JoinColumn(name="answer_id", referencedColumnName="id")
        )*/
        private List<Answer> answers;
        /*
        @ManyToOne
        @JoinColumn(name = "correct_answer_id")*/

        private Answer correctAnswer;
        private String content;
        private String imageUrl;
        private QuestionCategory category;



    public Question() {
    }

    public Question(Answer correctAnswer, String content, String imageUrl) {
        this.correctAnswer = correctAnswer;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = null;
        this.answers = new ArrayList<>();
        this.answers.add(correctAnswer);
    }


    public Question(Answer correctAnswer, String content, String imageUrl, QuestionCategory questionCategory) {
        this.correctAnswer = correctAnswer;
        this.content = content;
        this.imageUrl = imageUrl;
        this.answers = new ArrayList<>();
        this.answers.add(correctAnswer);
        this.category = questionCategory;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getContent() {
        return content;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }
    public String getLanguage(){
        return correctAnswer.getLanguage();
    }

    public void setAnswers(List<Answer> answers) {
        this.answers=answers;
    }

    public String getCorrectAnswerId() {
        return correctAnswer.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public void setCategory(QuestionCategory category) {
        this.category = category;
    }
}
