package com.backendfunction.bookmark.dto;

import com.backendfunction.bookmark.entity.BookMark;
import com.backendfunction.post.dto.PostReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookMarkDto {
    private Long id;
    private PostReqDto post;

    public static BookMarkDto formEntity(BookMark bookMark) {
        BookMarkDto dto = new BookMarkDto();
        dto.setId(bookMark.getId());
        if (bookMark.getPost() != null) {
            dto.setPost(new PostReqDto(bookMark.getPost()));
        }
        return dto;
    }

    public static BookMark formDto(BookMarkDto dto) {
        BookMark bookMark = new BookMark();
        bookMark.setId(dto.getId());
        return bookMark;
    }
}
