package com.mth.academicfeesystem.common.exception;

public class TokenNotValidException extends RuntimeException{
    public TokenNotValidException(String message){
        super(message);
    }
}
