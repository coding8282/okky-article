package org.okky.article.domain.repository;

import org.okky.article.domain.model.ArticleScrap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@RepositoryDefinition(domainClass = ArticleScrap.class, idClass = String.class)
public interface ArticleScrapRepository {
    void save(ArticleScrap scrap);
    @Query("from ArticleScrap s " +
            "where s.articleId = :articleId " +
            "and s.scrapperId = :scrapperId ")
    Optional<ArticleScrap> find(@Param("articleId") String articleId, @Param("scrapperId") String scrapperId);
    void delete(ArticleScrap scrap);
}
