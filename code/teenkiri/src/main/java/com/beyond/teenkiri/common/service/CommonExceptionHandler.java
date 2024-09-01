package com.beyond.teenkiri.common.service;

import com.beyond.teenkiri.common.dto.CommonErrorDto; // 커스텀 에러 응답을 정의하는 DTO 클래스
import org.springframework.http.HttpStatus; // HTTP 상태 코드를 나타내기 위해 사용
import org.springframework.http.ResponseEntity; // HTTP 응답을 나타내기 위해 사용
import org.springframework.web.bind.MethodArgumentNotValidException; // 유효성 검증 실패 예외 처리에 사용
import org.springframework.web.bind.annotation.ControllerAdvice; // 전역 예외 처리를 위한 어노테이션
import org.springframework.web.bind.annotation.ExceptionHandler; // 특정 예외를 처리하기 위한 어노테이션
import org.springframework.web.bind.annotation.ResponseStatus; // HTTP 응답 상태 코드를 명시하기 위한 어노테이션

import javax.persistence.EntityNotFoundException; // JPA 엔티티 검색 실패 시 발생하는 예외

@ControllerAdvice // 이 클래스는 모든 컨트롤러에 대한 전역 예외 처리기를 제공함을 나타냄
public class CommonExceptionHandler {

    // controller 단에서 발생하는 모든 EntityNotFoundException을 catch
    @ExceptionHandler(EntityNotFoundException.class) // EntityNotFoundException 발생 시 호출될 메서드
    public ResponseEntity<CommonErrorDto> entityNotFoundHandler(EntityNotFoundException e) {
        e.printStackTrace(); // 예외 스택 트레이스를 출력 (디버깅 목적)
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage()); // 에러 응답 생성
        return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND); // 404 NOT FOUND 응답 반환
    }

    @ExceptionHandler(IllegalArgumentException.class) // IllegalArgumentException 발생 시 호출될 메서드
    public ResponseEntity<CommonErrorDto> illegalArgumentHandler(IllegalArgumentException e) {
        e.getStackTrace(); // 예외 스택 트레이스를 출력
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()); // 에러 응답 생성
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST 응답 반환
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 유효성 검증 실패 예외 발생 시 호출될 메서드
    public ResponseEntity<CommonErrorDto> validHandler(MethodArgumentNotValidException e) {
        e.printStackTrace(); // 예외 스택 트레이스를 출력
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getFieldError().getDefaultMessage()); // 에러 응답 생성
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST 응답 반환
    }

    // IllegalStateException 처리
    @ExceptionHandler(IllegalStateException.class) // IllegalStateException 발생 시 호출될 메서드
    public ResponseEntity<CommonErrorDto> illegalStateException(IllegalStateException e) {
        e.getStackTrace(); // 예외 스택 트레이스를 출력
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()); // 에러 응답 생성
        return new ResponseEntity(commonErrorDto, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST 응답 반환
    }

    @ExceptionHandler(RuntimeException.class) // RuntimeException 발생 시 호출될 메서드
    public ResponseEntity<CommonErrorDto> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace(); // 예외 스택 트레이스를 출력
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()); // 에러 응답 생성
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST); // 400 BAD REQUEST 응답 반환
    }

    @ExceptionHandler(Exception.class) // 모든 종류의 Exception 발생 시 호출될 메서드 (최후의 보루)
    public ResponseEntity<CommonErrorDto> exceptionHandler(Exception e) {
        e.printStackTrace(); // 예외 스택 트레이스를 출력
        System.out.println("heer2"); // 디버깅용 출력
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "server"); // 일반 서버 에러 응답 생성
        return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR); // 500 INTERNAL SERVER ERROR 응답 반환
    }
}
