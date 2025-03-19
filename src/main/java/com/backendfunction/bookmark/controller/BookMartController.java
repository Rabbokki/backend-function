package com.backendfunction.bookmark.controller;

import com.backendfunction.account.entity.Account;
import com.backendfunction.bookmark.dto.BookMarkDto;
import com.backendfunction.bookmark.service.BookMarkService;
import com.backendfunction.global.security.user.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
public class BookMartController {

    private final BookMarkService bookMarkService;

    public BookMartController(BookMarkService bookMarkService) {
        this.bookMarkService = bookMarkService;
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BookMarkDto> bookMarkDtos = bookMarkService.findByAccount(userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body(bookMarkDtos);
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<?> addBookMark(@PathVariable("postId") Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null || userDetails.getAccount() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }
        Account account = userDetails.getAccount();
        bookMarkService.addBookmark(postId, account);
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<?> deleteById(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookMarkService.deleteByBookMarkId(bookId, userDetails.getAccount());
        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }
}
