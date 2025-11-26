package com.example.board.controller;

import com.example.board.dto.CommentDto;
import com.example.board.dto.PostDto;
import com.example.board.entity.Comment;
import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
//    private final PostRepository postRepository;

//    public PostController(PostRepository postRepository) {
//        this.postRepository = postRepository;
//        this.commentService = commentService;
//    }

    @GetMapping
    public String list(
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            Model model
    ) {
//        model.addAttribute("posts", postService.getAllPosts());
        Page<Post> postPage = postService.getPostsPage(pageable);
//        model.addAttribute("posts", postPage.getContent());

        int currentPage = postPage.getNumber();
        int totalPages = postPage.getTotalPages();
        int startPage = Math.max(0, currentPage - 5);
        int endPage = Math.min(totalPages - 1, currentPage + 5);

        model.addAttribute("postPage", postPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);

        model.addAttribute("comment", new CommentDto());
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", new PostDto());
        return "posts/form";
    }

    @PostMapping
    public String create(@ModelAttribute Post post) {
        // Post post = new Post("hi", "hello");
        postService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Post post) {
        postService.updatePost(id, post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/test/cache")
    public String testCache() {
        postService.testFirstLevelCache();
        return "redirect:/posts";
    }

    @GetMapping("/test/write-behind")
    public String testWriteBehind() {
        postService.testWriteBehind();
        return "redirect:/posts";
    }

    @GetMapping("/test/dirty-checking")
    public String testDirtyChecking() {
        postService.testDirtyChecking();
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam String keyword,
            @PageableDefault(sort="id") Pageable pageable,
            Model model
    ) {
        Page<Post> postPage = postService.searchPostsPage(keyword, pageable);

        int currentPage = postPage.getNumber();
        int totalPages = postPage.getTotalPages();
        int startPage = Math.max(0, currentPage - 5);
        int endPage = Math.min(totalPages - 1, currentPage + 5);

        model.addAttribute("postPage", postPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        return "posts/search";
    }

    // 최근 게시물 3개만 출력
    // /posts/recent

    @GetMapping("/recent")
    public String recent(Model model) {
        model.addAttribute("posts", postService.getRecentPosts());
        return "posts/list";
    }

    @GetMapping("/dummy")
    public String dummy() {
        postService.createDummyPosts(100);
        return "redirect:/posts";
    }

    @GetMapping("/more")
    public String more(
            @PageableDefault Pageable pageable,
            Model model
    ) {
        Slice<Post> postSlice = postService.getPostsSlice(pageable);
        model.addAttribute("postSlice", postSlice);
        return "posts/list-more";
    }


    // Comment
    @PostMapping("/{postId}/comments")
    public String createComment(
            @PathVariable Long postId,
            @ModelAttribute Comment comment
    ) {
        commentService.createComment(postId, comment);
        return "redirect:/posts/" + postId;
    }


}
