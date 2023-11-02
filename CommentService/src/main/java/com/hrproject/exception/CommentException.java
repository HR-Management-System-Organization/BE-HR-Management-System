package com.hrproject.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{

    private final ErrorType errorType;

    public CommentException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }

    public CommentException(ErrorType errorType){
        this.errorType = errorType;
    }
}
