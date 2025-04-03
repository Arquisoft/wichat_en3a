package com.uniovi.userservice.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Basic enity that represents the Users in the application
 * It contains:
 * -ID: an unique identifier used by the DB
 * -Name: username
 * -Email: email of the user. It must be unique
 * -Password: password of the user. Encrypted by the webapp
 * -Correct: a boolean that indicates if the user of correct. It will be used by the
 * webapp to check the success of operations (adding an already existing user would return a User with this
 * value set to false to the application).
 */
@Document(collection="users")
public class User {

    @Id
    private String id; //No need to add GeneratedValue since MongoDB handles that

    private String name;
    private String email;
    private String password;
    private boolean correct;

    public User() {}

    public User(String name, String email, String password, boolean correct) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.correct = correct;
    }

    public User(String id, String name, String email, String password, boolean correct) {
        this(name, email, password, correct);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
