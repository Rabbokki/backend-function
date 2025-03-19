package com.backendfunction.Cart.repository;

import com.backendfunction.Cart.entity.Cart;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.dto.AccountDto;
import com.backendfunction.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByLiquorAndAccount(Liquor liquor, Account account);


    Cart findByidAndAccount(Long id, Account account);
}
