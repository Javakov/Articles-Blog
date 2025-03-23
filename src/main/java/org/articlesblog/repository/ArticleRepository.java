package org.articlesblog.repository;

import org.articlesblog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    @Query("""
        SELECT a FROM Article a\s
        WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(a.description) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(a.text) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(a.author) LIKE LOWER(CONCAT('%', :searchText, '%'))
           OR LOWER(a.label) LIKE LOWER(CONCAT('%', :searchText, '%'))
   \s""")
    Page<Article> searchByText(@Param("searchText") String searchText, Pageable pageable);
}
