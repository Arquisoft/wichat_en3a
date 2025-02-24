package com.uniovi.wichatwebapp.entities;

public class User {

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
}
