package com.backendfunction.like.service;

import com.backendfunction.account.entity.Account;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.commet.repository.CommentRepository;
import com.backendfunction.global.dto.ResponseDto;
import com.backendfunction.like.entity.CommentLike;
import com.backendfunction.like.entity.PostLike;
import com.backendfunction.like.repository.CommentLikeRepository;
import com.backendfunction.like.repository.PostLikeRepository;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseDto<?> addPostLike(Long postId, Account account) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        System.out.println("////// Now printing post: " + post);

        // Check if already liked using existsByAccountAndPost
        if (postLikeRepository.existsByAccountAndPost(account, post)) {
            System.out.println("////// Now printing if like already exists: " + post);
            return ResponseDto.fail("ALREADY LIKED", "Post already liked");
        }

        PostLike newLike = new PostLike(post, account);
        System.out.println("////// Now printing newLike: " + newLike);
        postLikeRepository.save(newLike);
        return ResponseDto.success("Post liked successfully");
    }

    @Transactional
    public ResponseDto<?> removePostLike(Long postId, Account account) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Use findByAccountAndPost to find the like
        PostLike existingLike = postLikeRepository.findByAccountAndPost(account, post)
                .orElseThrow(() -> new IllegalArgumentException("Post not liked"));

        postLikeRepository.delete(existingLike);
        return ResponseDto.success("Like removed successfully");
    }

    public boolean isPostLiked(Long postId, Account account) {
        return postLikeRepository.findByAccountAndPost(account, postRepository.findById(postId).orElse(null)).isPresent();
    }


    @Transactional
    public ResponseDto<?> commentLike(Long commentId, Account account) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new RuntimeException("댓글 not found"));

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByAccountAndComment(account, comment);

        int likeSize = comment.getLikeSize();
        String isLike;

        if(optionalCommentLike.isEmpty()){
            CommentLike commentLike = new CommentLike(comment,account);
            commentLikeRepository.save(commentLike);
            comment.updateLikeSize(likeSize+1);
            isLike = "좋아요 완료";
        }else {
            commentLikeRepository.delete(optionalCommentLike.get());
            comment.updateLikeSize(likeSize-1);
            isLike = "좋아요 취소";
        }
        return ResponseDto.success(isLike);
    }
}
