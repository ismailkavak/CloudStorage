package com.example.CloudStorage.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    // Handle validation errors
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            //MethodArgumentNotValidException object keeps details of validation errors
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        System.out.println("Validation exception handler çalıştı!");

        // Collect all field errors
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        // getBindingResult() -> There is all error knowledges like field errors, object-level errors, rejected value etc.
        // getFieldErrors()-> Takes just field level errors from getBindingResult().
        // stream() -> takes getDefaultMessage()
        // collect() -> Collects the contents in the Stream as a List

        // Combine error messages
        String errorMessage = String.join(" || ", fieldErrors);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

//    General Exception handler (optional - for debug)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneralException(Exception ex) {
//        System.out.println("General exception handler worked: " + ex.getMessage());
//        ex.printStackTrace();
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("There is a mistake: " + ex.getMessage());
//    }
}