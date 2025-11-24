package com.example.board.repository;

import com.example.board.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    // @RequiredArgsConstructor를 통해 자동생성
//    public PostRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    private final RowMapper<PostDto> rowMapper = (rs, rowNum) -> {
        return new PostDto(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    };

    // 전체 조회
    public List<PostDto> findAll() {
        String sql = "SELECT * FROM post";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // 상세조회
    public PostDto findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";

        // queryForObject => 단일 행 조회
        PostDto post = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return post;
    }



}
