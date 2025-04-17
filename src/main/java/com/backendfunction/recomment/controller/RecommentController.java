package com.backendfunction.recomment.controller;

import com.backendfunction.commet.dto.CommentReqDto;
import com.backendfunction.commet.service.CommentService;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.global.security.user.UserDetailsImpl;
import com.backendfunction.recomment.dto.RecommentReqDto;
import com.backendfunction.recomment.service.RecommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recomment")
@RequiredArgsConstructor
public class RecommentController {
    private final RecommentService recommentService;

    @PostMapping("/{commentId}")
    public ResponseDto<?> createRecomment(@PathVariable("commentId") Long commentId,
                                          @RequestBody @Valid RecommentReqDto commentReqDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseDto.success(recommentService.createRecomment(commentId,commentReqDto,userDetails.getAccount()));
    }

    @DeleteMapping("/{recommentId}")
    public ResponseDto<?> deleteRecomment(@PathVariable("recommentId") Long recommentId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recommentService.deleteRecomment(recommentId,userDetails.getAccount());
    }
}
