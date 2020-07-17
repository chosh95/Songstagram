package com.cho.songstagram.service;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsService postsService;
    private final UsersService usersService;

    @Transactional
    public void save(Long postId, Long userId, CommentDto commentDto) {
        Posts posts = postsService.findById(postId)
                .orElse(new Posts());
        Users users = usersService.findById(userId)
                .orElse(new Users());
        Comments comments = Comments.builder()
                .content(commentDto.getComment())
                .users(users)
                .posts(posts)
                .build();
        commentsRepository.save(comments);
    }

    @Transactional
    public void delete(Long commentId){
        Comments comments = commentsRepository.findById(commentId)
                .orElse(new Comments());
        commentsRepository.delete(comments);
    }

    public Optional<Comments> findById(Long commentId){
        return commentsRepository.findById(commentId);
    }
    public List<Comments> findCommentsByPosts(Posts posts){
        return commentsRepository.findCommentsByPosts(posts);
    }
    public CommentDto convertToDto(Comments comments){
        return CommentDto.builder()
                .comment(comments.getContent())
                .commentId(comments.getId())
                .userId(comments.getUsers().getId())
                .userName(comments.getUsers().getName())
                .userPicture(comments.getUsers().getPicture())
                .build();
    }
}
