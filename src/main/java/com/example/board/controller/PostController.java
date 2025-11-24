package com.example.board.controller;

import com.example.board.dto.PostDto;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

//    public PostController(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostDto post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", new PostDto());
        return "posts/form";
    }

    @PostMapping
    public String create(@ModelAttribute PostDto postDto) {
        postRepository.save(postDto);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        PostDto post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "posts/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute PostDto postDto) {
        postRepository.update(id, postDto);
        return "redirect:/posts/" + id;
    }




}
