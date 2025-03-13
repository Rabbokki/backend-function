package com.backendfunction.account.entity;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.account.dto.AccountReqDto;
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Cart> carts = new ArrayList<>();

    public Account(AccountReqDto accountReqDto) {

        this.email = accountReqDto.getEmail();
        this.password = accountReqDto.getPassword();
        this.nickname = accountReqDto.getNickname();
    }
}
