package com.cho.songstagram.service;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.dto.CommentDto;
import com.cho.songstagram.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsService postsService;
    private final UsersService usersService;

    //유저, 게시글 가져와서 빌터 패턴으로 댓글 생성 후 db에 저장
    @Transactional
    public void save(Long postId, Long userId, CommentDto commentDto) {
        Posts posts = postsService.findById(postId).orElse(new Posts());
        Users users = usersService.findById(userId).orElse(new Users());

        Comments comments = Comments.builder()
                .content(commentDto.getComment())
                .users(users)
                .posts(posts)
                .build();

        commentsRepository.save(comments);

        posts.getCommentsList().add(comments);
        users.getComemntsList().add(comments);
    }

    // 아이디에 해당하는 댓글 db에서 삭제
    @Transactional
    public void delete(Long commentId){
        Comments comments = commentsRepository.findById(commentId).orElse(new Comments());

        comments.getPosts().getCommentsList().remove(comments);
        comments.getUsers().getComemntsList().remove(comments);

        commentsRepository.delete(comments);
    }

    //id로 댓글 가져오기
    public Optional<Comments> findById(Long commentId){
        return commentsRepository.findById(commentId);
    }

    //해당 게시글에 있는 댓글 목록 가져오기
    public List<Comments> findCommentsByPosts(Posts posts){
        return commentsRepository.findCommentsByPosts(posts);
    }

    //댓글 dto로 전환 후 반환
    public CommentDto convertToDto(Comments comments){
        return CommentDto.builder()
                .comment(comments.getContent())
                .commentId(comments.getId())
                .userId(comments.getUsers().getId())
                .userName(comments.getUsers().getName())
                .userPicture(comments.getUsers().getPicture())
                .createdDate(comments.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
