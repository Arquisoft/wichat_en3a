package entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scores")
public class Score {

    @Id
    private String id;

    private String user_id;
    private String user;
    private String category;
    private int score;
    private int rightAnswers;
    private int wrongAnswers;

    public Score(){
    }

    public Score (String user, String category, int score, int rightAnswers, int wrongAnswers) {
        this("noId",user,category,score,rightAnswers,wrongAnswers);
    }

    public Score(String user_id, String user, String category, int score, int rightAnswers, int wrongAnswers) {
        this.user_id = user_id;
        this.user = user;
        this.category = category;
        this.score = score;
        this.rightAnswers = rightAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(int rightAnswers) {
        this.rightAnswers = rightAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
