package com.backendfunction.Liquor.entity;

import com.backendfunction.Liquor.dto.LiquorDto;
import com.backendfunction.account.entity.Account;
import com.backendfunction.global.category.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "liquor")
public class Liquor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;
    private Integer price;
    private Integer stock;
    @Column(length = 50)
    private String country;
    private int count;
    private int totalPrice;
    //category와 매핑 from JJJ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id")
    @JsonBackReference
    private Category category;

    public Liquor(LiquorDto dto, Account account) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.country = dto.getCountry();
        this.count = dto.getCount();
        this.totalPrice = dto.getTotalPrice();
    }


    public Liquor() {

    }
}
