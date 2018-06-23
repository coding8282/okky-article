package org.okky.article.resource;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.article.domain.repository.ArticleMapper;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.dto.ArticleDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = PRIVATE)
class ArticleResourceInternal {
    ArticleMapper mapper;
    ArticleRepository repository;
    ContextHolder holder;

    @GetMapping(value = "/articles/{articleId}", produces = APPLICATION_JSON_VALUE)
    ArticleDto get(@PathVariable String articleId) {
        return mapper.selectOne(articleId, holder.getId());
    }

    @GetMapping(value = "/articles/{articleId}/writers/{writerId}/match")
    boolean match(@PathVariable String articleId, @PathVariable String writerId) {
        return repository.existsByIdAndWriterId(articleId, writerId);
    }
}
