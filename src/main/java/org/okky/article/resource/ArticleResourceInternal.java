package org.okky.article.resource;

import lombok.AllArgsConstructor;
import org.okky.article.application.ArticleService;
import org.okky.article.domain.repository.ArticleMapper;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.dto.ArticleDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("/internal")
class ArticleResourceInternal {
    ArticleService service;
    ArticleMapper mapper;
    ArticleRepository repository;

    @GetMapping(value = "/articles/{articleId}", produces = APPLICATION_JSON_VALUE)
    ArticleDto get(@PathVariable String articleId) {
        return mapper.selectOne(articleId, ContextHelper.getId());
    }
}
