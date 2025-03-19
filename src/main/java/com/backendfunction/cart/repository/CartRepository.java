package com.backendfunction.cart.repository;

import com.backendfunction.cart.entity.Cart;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByLiquorAndAccount(Liquor liquor, Account account);


    Cart findByidAndAccount(Long id, Account account);


    Cart findByLiquorIdAndAccount(Long id, Account account);

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.liquor " +
            "WHERE c.account.id = :accountId")
    List<Cart> findByAccountIdWithLiquor(@Param("accountId") Long id);
}
