package com.backendfunction.account.entity;

import com.backendfunction.account.dto.AccountReqDto;
import com.backendfunction.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;


    public Account(AccountReqDto accountReqDto) {
        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
    }
}
