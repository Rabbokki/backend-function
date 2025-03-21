package com.backendfunction.cart.entity;

import com.backendfunction.cart.dto.CartReqDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import com.backendfunction.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int count; //총 상품 개수
    @Column(nullable = false)
    private int price; //총 가격
    @ManyToOne
    @JoinColumn(name = "account_id" , nullable = true)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    public Cart(CartReqDto dto, Account account) {
        this.count = dto.getCount();
        this.price = dto.getPrice();
        this.account = account;
    }

    public Cart(Post post, Account account, int count, int price) {
        this.post = post;
        this.account = account;
        this.price = price;
        this.count = count;
    }

    public Cart() {

    }
}
