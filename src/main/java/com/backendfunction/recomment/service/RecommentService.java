package com.backendfunction.recomment.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.dto.CommentReqDto;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.commet.repository.CommentRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.recomment.dto.RecommentReqDto;
import com.backendfunction.recomment.dto.RecommentResDto;
import com.backendfunction.recomment.entity.Recomment;
import com.backendfunction.recomment.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommentService {
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    @Transactional
    public ResponseDto<?> createRecomment(Long commentId, RecommentReqDto ReqDto, Account account) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new RuntimeException("대댓글 작성 게시글 없음"));
        Recomment recomment = new Recomment(comment,account,ReqDto.getContent());
        recommentRepository.save(recomment);
        return ResponseDto.success(RecommentResDto.fromEntity(recomment));
    }

    @Transactional
    public ResponseDto<?> deleteRecomment(Long id, Account account) {
        Recomment recomment = recommentRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("대댓글 못찾음")
        );
        if(recomment.getAccount().getId().equals(account.getId())){
            recommentRepository.delete(recomment);
        }else {
            throw new RuntimeException("대댓글 삭제 회원 아님");
        }
        return ResponseDto.success("삭제 성공");
    }
}
