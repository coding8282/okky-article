package org.okky.article.domain.service;

import lombok.AllArgsConstructor;
import org.okky.article.domain.model.Article;
import org.okky.article.resource.ContextHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleSecurityInspector {
    private ArticleConstraint constraint;

    public boolean isThisWriter(String articleId) {
        Article article = constraint.checkExistsAndGet(articleId);
        String writerId = article.getWriterId();
        String requesterId = ContextHelper.getId();
        return requesterId.equals(writerId);
    }
}
