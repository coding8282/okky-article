package org.okky.article.domain.repository;

import org.okky.article.domain.model.Article;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

@RepositoryDefinition(domainClass = Article.class, idClass = String.class)
public interface ArticleRepository extends RevisionRepository<Article, String, Long> {
    void save(Article article);
    Optional<Article> findById(String id);
    boolean existsById(String articleId);
    long countByWriterId(String writerId);
    long countByBoardId(String boardId);
}
