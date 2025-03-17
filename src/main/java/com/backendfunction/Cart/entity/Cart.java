package com.backendfunction.Cart.entity;

import com.backendfunction.Cart.dto.CartRedDto;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cart")
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false)
    private int price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(fetch = FetchType.LAZY , mappedBy = "cart",cascade = CascadeType.ALL)
    private List<Liquor> liquors = new ArrayList<>();


    public Cart(Long id, int count, int price, Account account ) {
        this.id = id;
        this.count = count;
        this.price = price;
        this.account = account;
    }

    public Cart(CartRedDto dto, Account account) {
        this.count = dto.getCount();
        this.account = account;
        this.price = dto.getPrice();
    }


}
