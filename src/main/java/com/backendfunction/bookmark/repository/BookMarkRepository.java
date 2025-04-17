package com.backendfunction.bookmark.repository;

import com.backendfunction.account.entity.Account;
import com.backendfunction.bookmark.entity.BookMark;
import com.backendfunction.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    @Query("SELECT b FROM BookMark b " +
            "LEFT JOIN FETCH b.post " +
            "WHERE b.account.id = :accountId")
    List<BookMark> findByAccountId(@Param("accountId") Long id);

    BookMark findByPostAndAccount(Post post, Account account);

    BookMark findByIdAndAccount(Long bookId, Account account);
}
