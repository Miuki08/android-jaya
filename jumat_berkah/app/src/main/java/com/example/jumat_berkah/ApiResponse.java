package com.example.jumat_berkah;

public class ApiResponse {
    private String message;
    private String error;
    private User user; // tambahkan ini

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public User getUser() {
        return user;
    }

    public boolean isSuccess() {
        return message != null && !message.isEmpty();
    }


}
