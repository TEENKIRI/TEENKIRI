package com.beyond.board.common;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class CommonErrorDto {
    private int status_code;
    private String status_message;

    public CommonErrorDto(HttpStatus httpStatus, String status_message) {
        this.status_code = httpStatus.value();
        this.status_message = status_message;
    }
}
