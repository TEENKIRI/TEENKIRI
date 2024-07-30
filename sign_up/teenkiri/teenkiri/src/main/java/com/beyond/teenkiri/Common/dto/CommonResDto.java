package com.beyond.teenkiri.Common.dto;

import lombok.Getter;

@Getter
public class CommonResDto<T> {
    private T data;

    public CommonResDto(T data) {
        this.data = data;
    }
}
