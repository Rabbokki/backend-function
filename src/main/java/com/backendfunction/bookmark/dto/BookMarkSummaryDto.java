package com.backendfunction.bookmark.dto;

import com.backendfunction.bookmark.entity.BookMark;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookMarkSummaryDto {
    private Long id;
    private Long postId;
    private String postTitle;
    private LocalDateTime createdAt;

    public BookMarkSummaryDto(BookMark bookMark) {
        this.id = bookMark.getId();
        this.postId = bookMark.getPost().getId();
        this.postTitle = bookMark.getPost().getTitle();
        this.createdAt = bookMark.getPost().getCreatedAt();
    }
}
