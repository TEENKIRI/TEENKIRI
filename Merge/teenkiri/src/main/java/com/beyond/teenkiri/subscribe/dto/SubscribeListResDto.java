package com.beyond.teenkiri.subscribe.dto;

import com.beyond.teenkiri.subscribe.domain.WishType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeListResDto {
    private String userEmail;
    private String subjectTitle;
    private WishType wishType;
}
