package edu.JavaExam.model;




public class LoginRequest {
    private String pseudo;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "pseudo='" + pseudo + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
} 