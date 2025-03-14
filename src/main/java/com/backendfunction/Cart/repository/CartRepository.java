package com.backendfunction.Cart.repository;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Liquor.entity.Liquor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
