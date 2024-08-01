package com.beyond.teenkiri.common;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
// 기본적으로 Entity는 상속관계가 불가능하여, 해당 어노테이션을 붙여야 상속관계 성립 가능
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

}
