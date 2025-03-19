package com.backendfunction.bookmark.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.bookmark.dto.BookMarkDto;
import com.backendfunction.bookmark.entity.BookMark;
import com.backendfunction.bookmark.repository.BookMarkRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;

    public BookMarkService(BookMarkRepository bookMarkRepository, PostRepository postRepository) {
        this.bookMarkRepository = bookMarkRepository;
        this.postRepository = postRepository;
    }


    public List<BookMarkDto> findByAccount(Account account) {
        List<BookMark> bookMarks = bookMarkRepository.findByAccountId(account.getId());
        return bookMarks.stream().map(x -> BookMarkDto.formEntity(x)).toList();
    }

    public ResponseDto<?> addBookmark(Long postId, Account account) {
        Post post = postRepository.findPostByIdAndAccount(postId, account);
        if (ObjectUtils.isEmpty(post)) {
            return ResponseDto.fail("100", "찾을수 없는 데이터입니다");
        }
        System.out.println(post);
        BookMark bookMark = bookMarkRepository.findByPostAndAccount(post, account);
        if (bookMark != null) {
            bookMarkRepository.deleteById(bookMark.getId());
            return ResponseDto.success(bookMark);
        } else {
            BookMark sad = new BookMark();
            sad.setPost(post);
            sad.setAccount(account);
            bookMarkRepository.save(sad);

            return ResponseDto.success(bookMark);

        }

    }

    public ResponseDto<?> deleteByBookMarkId(Long bookId, Account account) {
        BookMark bookMark = bookMarkRepository.findByIdAndAccount(bookId, account);
        if (bookMark == null) return ResponseDto.fail("100", "삭제할수 없습니다");
        bookMarkRepository.delete(bookMark);
        return ResponseDto.success("삭제 성공");
    }
}
