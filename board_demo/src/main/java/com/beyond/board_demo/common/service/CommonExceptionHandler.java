package com.beyond.board_demo.common.service;

import com.beyond.board_demo.common.dto.CommonErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class CommonExceptionHandler {

    //    controller 단에서 발생하는 모든 EntityNotFoundException catch
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> entityNotFoundHandler(EntityNotFoundException e) {
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> illegalArgumentHandler(IllegalArgumentException e) {
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(commonErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorDto> methodArgumentNotValidException (MethodArgumentNotValidException e) {
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
    }
}
