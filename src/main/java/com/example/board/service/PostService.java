package com.example.board.service;

import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

//    @Transactional(readOnly=true)
    public Post getPostById(Long id) {
        return postRepository.findById(id);

        // readOnly = fasle
        // 1. 엔티티 조회
        // 2. 스냅샷 저장
        // 3. 트랜젝션이 끝날 때 비교
        // 4. 변경이 있으면 update

        // readOnly = true
        // 1. 엔티티 조회

    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost) {
        Post post = getPostById(id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        return postRepository.update(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }



}
