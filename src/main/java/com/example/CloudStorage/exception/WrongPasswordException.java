package com.example.CloudStorage.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message){
        super(message);
    }
}
