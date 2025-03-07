package com.backendfunction.commet.service;

import com.backendfunction.commet.dto.CommentDto;
import com.backendfunction.commet.entity.Comment;
import com.backendfunction.commet.repository.CommentRepository;
import com.backendfunction.post.dto.PostDto;
import com.backendfunction.post.entity.Post;
import com.backendfunction.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    EntityManager em;
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
    }



    public List<CommentDto> findAll() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(x -> CommentDto.fromEntity(x)).toList();
    }

    public Map<String, Object> findByCommentId(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        Map<String, Object> data = new HashMap<>();
        if (ObjectUtils.isEmpty(comment)) {
            data.put("postId", null);
            data.put("dto", null);
        } else {
            data.put("postId", comment.getPost().getId());
            data.put("dto", CommentDto.fromEntity(comment));
        }
        return data;
    }


    public void insertComment(CommentDto dto, Long id) {
        Comment comment = CommentDto.fromDto(dto);
        Post post = em.find(Post.class, id);
        comment.setPost(post);

        commentRepository.save(comment);
    }

    public void updateByCommentId(CommentDto dto) {
        Comment comment = em.find(Comment.class, dto.getId());
        comment.setNickname(dto.getNickname());
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
    }

    public void deleteByCommentId(Long id) {
        commentRepository.deleteById(id);

    }
}
