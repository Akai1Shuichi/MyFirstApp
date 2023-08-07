package com.example.myfirstapp.api.model;

public class Token {
    private String message, token ;

    public Token(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
