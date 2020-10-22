package com.cho.songstagram.repository;

import com.cho.songstagram.domain.Comments;
import com.cho.songstagram.domain.Posts;
import com.cho.songstagram.domain.Users;
import com.cho.songstagram.makeComponent.MakeComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CommentsRepositoryTest {

    @Autowired UsersRepository usersRepository;
    @Autowired PostsRepository postsRepository;
    @Autowired CommentsRepository commentsRepository;
    @Autowired MakeComponent makeComponent;

    @Test
    public void 게시글에_작성한_댓글목록_가져오기(){
        Users users = makeComponent.makeUsers();
        usersRepository.save(users);

        Posts posts = makeComponent.makePosts(users);
        postsRepository.save(posts);

        Comments comments = makeComponent.makeComments(users,posts);
        commentsRepository.save(comments);

        Comments comments2 = makeComponent.makeComments(users,posts);
        commentsRepository.save(comments2);

        List<Comments> commentsList = new ArrayList<>();
        commentsList.add(comments);
        commentsList.add(comments2);

        assertEquals(commentsList,commentsRepository.findCommentsByPosts(posts));
    }
}