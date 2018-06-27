package org.okky.article.domain.repository;

import org.okky.article.domain.model.Article;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

@RepositoryDefinition(domainClass = Article.class, idClass = String.class)
public interface ArticleRepository extends RevisionRepository<Article, String, Long> {
    boolean existsById(String articleId);
    boolean existsByIdAndWriterId(String articleId, String writerId);
    void save(Article article);
    Optional<Article> findById(String id);
    long countByWriterId(String writerId);
    long countByBoardId(String boardId);
}
