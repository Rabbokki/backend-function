package com.backendfunction.Liquor.entity;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.global.category.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
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

    //category와 매핑 from JJJ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id")
    @JsonBackReference
    private Category category;
}
