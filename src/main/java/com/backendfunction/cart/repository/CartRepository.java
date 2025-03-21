package com.backendfunction.cart.repository;

import com.backendfunction.cart.entity.Cart;
import com.backendfunction.Liquor.entity.Liquor;
import com.backendfunction.account.entity.Account;
import com.backendfunction.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByidAndAccount(Long id, Account account);



    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.post " +
            "WHERE c.account.id = :accountId")
    List<Cart> findByAccountIdWithPost(@Param("accountId") Long id);

    Cart findByPostAndAccount(Post post, Account account);

    Cart findByPostIdAndAccount(Long postId, Account account);
}
