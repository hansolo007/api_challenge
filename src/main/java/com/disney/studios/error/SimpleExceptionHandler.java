package com.disney.studios.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.disney.studios.pojo.Response;


@ControllerAdvice
public class SimpleExceptionHandler{
	
	@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleException(NotFoundException e) {
		Response notFoundResponse = new Response(Response.NOTFOUND,e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
    }   

}
