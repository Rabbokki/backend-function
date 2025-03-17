package com.backendfunction.Cart.entity;

import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int Count;
    @Column(nullable = false)
    private int price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquor_id")  // "liquor_id"라는 이름으로 외래 키를 설정
    private Liquor liquor;

}
