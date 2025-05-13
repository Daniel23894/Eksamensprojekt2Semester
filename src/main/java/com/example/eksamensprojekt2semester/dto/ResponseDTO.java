package com.example.eksamensprojekt2semester.dto;

import java.time.LocalDateTime;

public class ResponseDTO<T> {

    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Static helper methods for common responses
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>("SUCCESS", message, data);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>("ERROR", message, null);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
