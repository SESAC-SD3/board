package com.example.board.controller;

import com.example.board.dto.PostDto;
import com.example.board.entity.Post;
import com.example.board.repository.PostRepository;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
//    private final PostRepository postRepository;

//    public PostController(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }

    @GetMapping
    public String list(
            @PageableDefault(
                    size = 20,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            Model model
    ) {
//        model.addAttribute("posts", postService.getAllPosts());
        Page<Post> postPage = postService.getPostsPage(pageable);
        model.addAttribute("posts", postPage.getContent());

        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
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
    public String search(@RequestParam String keyword, Model model) {
        List<Post> posts = postService.searchPostsByTitleOrContent(keyword);
        model.addAttribute("posts", posts);
        return "posts/list";
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


}
