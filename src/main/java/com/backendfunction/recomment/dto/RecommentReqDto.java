package com.backendfunction.recomment.dto;

import com.backendfunction.recomment.entity.Recomment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommentReqDto {
    @NotBlank(message = "대댓글을 입력해 주세요~")
    private String content;

    public static RecommentReqDto fromEntity(Recomment recomment){
        return new RecommentReqDto(recomment.getContent());
    }
}
