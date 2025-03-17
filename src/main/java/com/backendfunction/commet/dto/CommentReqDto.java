package com.backendfunction.commet.dto;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.post.entity.Post;
import com.backendfunction.recomment.dto.RecommentResDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class CommentReqDto {
    private Long id;
    @NotBlank(message = "댓글을 입력해 주세요.")
    private String content;
    private List<RecommentResDto> resDtoList;

    public static CommentReqDto fromEntity(Comment comment){
        return new CommentReqDto(
                comment.getId(),
                comment.getContent(),
                comment.getRecomments().stream().map(
                        RecommentResDto::fromEntity
                ).collect(Collectors.toList())
        );
    }

    public static Comment fromDto(CommentDto commentDto, Post post,
                                  Account account){
        return new Comment(
            commentDto.getContent(),
                post,
                account
        );
    }
}
