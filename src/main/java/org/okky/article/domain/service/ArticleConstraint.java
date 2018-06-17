package org.okky.article.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.article.domain.model.Article;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.share.execption.ExternalServiceError;
import org.okky.share.execption.ModelConflicted;
import org.okky.share.execption.ModelNotExists;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ArticleConstraint {
    ArticleRepository repository;
    RestTemplate template;

    public void checkExists(String articleId) {
        checkExistsAndGet(articleId);
    }

    public Article checkExistsAndGet(String articleId) {
        return repository
                .findById(articleId)
                .orElseThrow(() -> new ModelNotExists(format("해당 게시글은 존재하지 않습니다: '%s'", articleId)));
    }

    public void rejectRemoveIfHasReplies(String articleId) {
        try {
            ResponseEntity<Boolean> result = template.getForEntity(format("/articles/%s/replies/exists", articleId), Boolean.class);
            if (result.getBody())
                throw new ModelConflicted(format("답변이 존재하기 때문에 게시글을 삭제할 수 없습니다: '%s'", articleId));
        } catch (HttpStatusCodeException e) {
            throw new ExternalServiceError(new String(e.getResponseBodyAsByteArray()));
        }
    }
}
