package org.okky.article.domain.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.okky.article.domain.repository.dto.ArticleDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ArticleMapper {
    ArticleDto selectOne(@Param("id") String id, @Param("myId") String myId);
    List<ArticleDto> select(Map<String, Object> params);
    List<ArticleDto> selectFromScrap(Map<String, Object> params);
    List<ArticleDto> selectFromTag(Map<String, Object> params);
    long count(Map<String, Object> params);
    long countFromScrap(Map<String, Object> params);
    long countFromTag(Map<String, Object> params);
    long countScrapByArticleId(@Param("articleId") String articleId);
}