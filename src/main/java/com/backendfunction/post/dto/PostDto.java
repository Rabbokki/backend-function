//package com.backendfunction.post.dto;
//
//import com.backendfunction.commet.dto.CommentDto;
//import com.backendfunction.global.image.entity.Image;
//import com.backendfunction.post.entity.Post;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class PostDto {
//    private Long id;
//    private String content;
//    private String title;
//    private int price;
//    private List<CommentDto> commentDtos = new ArrayList<>();
//    private List<String> imgs;
//
//
////    public PostDto(Post post) {
////        this.id = post.getId();
////    }
//public PostDto(Post post) {
//    this.id = post.getId();
//    this.content = post.getContent();
//    this.title = post.getTitle();
//    this.price = post.getPrice();
//    this.commentDtos = post.getCommentList().stream()
//            .map(CommentDto::fromEntity)
//            .collect(Collectors.toList());
//    this.imgs = post.getImages().stream()
//            .map(Image::getImage)
//            .collect(Collectors.toList());
//}
//
//    public static PostDto fromEntity(Post post) {
//        return new PostDto(
//                post.getId(),
//                post.getContent(),
//                post.getTitle(),
//                post.getPrice(),
//                post.getCommentList().stream().map(x -> CommentDto.fromEntity(x)).toList(),
//                post.getImages().stream().map(image->image.getImage()).collect(Collectors.toList())
//        );
//    }
//
//    public static Post fromDto(PostDto dto) {
//        Post post = new Post();
//        post.setId(dto.getId());
//        post.setContent(dto.getContent());
//        post.setTitle(dto.getTitle());
//        post.setPrice(dto.getPrice());
//        return post;
//    }
//
//}
