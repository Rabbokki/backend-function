package com.backendfunction.recomment.dto;

import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.recomment.entity.Recomment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommentResDto {
    private Long id;
    private String content;
    private AccountReqDto accountReqDto;

    public static RecommentResDto fromEntity(Recomment recomment){
        return new RecommentResDto(
                recomment.getId(),
                recomment.getContent(),
                AccountReqDto.fromEntity(recomment.getAccount())
        );
    }
}
