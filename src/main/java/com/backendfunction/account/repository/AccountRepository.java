package com.backendfunction.account.repository;

import com.backendfunction.account.entity.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.carts WHERE a.email = :email")
    Optional<Account> findAccountWithCartsByEmail(@Param("email") String email);


    boolean existsByEmail(String targetEmail);
    Optional<Account> findByProviderAndProviderId(String provider, String providerId);

}
