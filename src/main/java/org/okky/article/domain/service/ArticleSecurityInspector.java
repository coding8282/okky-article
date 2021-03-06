package org.okky.article.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.article.domain.model.Article;
import org.okky.article.resource.ContextHolder;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ArticleSecurityInspector {
    ArticleConstraint constraint;
    ContextHolder holder;

    public boolean isThisWriter(String articleId) {
        Article article = constraint.checkExistsAndGet(articleId);
        String writerId = article.getWriterId();
        String requesterId = holder.getId();
        return requesterId.equals(writerId);
    }
}
