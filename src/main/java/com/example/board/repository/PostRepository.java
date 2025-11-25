package com.example.board.repository;

import java.util.List;
import com.example.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 기본 CRUD 메서드

    // 저장 (INSERT or UPDATE)
    // Post save(Post entity);

    // 조회
    // Optional<Post> findById(Long id);
    // List<Post> findAll();
    // List<Post> findAll(Sort sort);

    // 삭제
    // void deleteById(Long id);
    // void delete(Post entity);

    // 개수 조회
    // long count();
    // 존재 여부 확인
    // boolean existsById(Long id);

    // findBy + 필드명 + 조건

    // LIKE %keyword%
    List<Post> findByTitleContaining(String keyword);

    // @Query 방식
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    List<Post> searchByTitle(@Param("keyword") String keyword);

    // LIKE keyword%
    List<Post> findByTitleStartingWith(String keyword);

    // >
    List<Post> findByIdGreaterThan(Long id);

    // ORDER BY id DESC
    List<Post> findAllByOrderByIdDesc();

    // 제목 or 내용 으로 검색
    List<Post> findByTitleContainingOrContentContaining(
            String titleKeyword, String contentKeyword
    );

    // 제목 or 내용 으로 검색
    @Query("""
        SELECT p FROM Post p 
        WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% 
        ORDER BY p.createdAt DESC
    """)
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    @Query(value="""
        SELECT * FROM post 
        WHERE title LIKE %:keyword% 
        ORDER BY id DESC
    """, nativeQuery=true)
    List<Post> searchByTitleNative(@Param("keyword") String keyword);
}










