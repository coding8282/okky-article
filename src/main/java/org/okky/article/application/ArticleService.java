package org.okky.article.application;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.article.application.command.ModifyArticleCommand;
import org.okky.article.application.command.MoveArticleCommand;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.event.DomainEventPublisher;
import org.okky.article.domain.model.Article;
import org.okky.article.domain.model.ArticleScrap;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.ArticleScrapRepository;
import org.okky.article.domain.service.ArticleConstraint;
import org.okky.article.domain.service.BoardConstraint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ArticleService {
    ArticleRepository articleRepository;
    ArticleScrapRepository articleScrapRepository;
    BoardConstraint boardConstraint;
    ArticleConstraint articleConstraint;
    ModelMapper mapper;

    /**
     * @return 쓴 게시글 id
     */
    public String write(WriteArticleCommand cmd) {
        boardConstraint.checkExists(cmd.getBoardId());
        Article article = mapper.toModel(cmd);
        articleRepository.save(article);
        return article.getId();
    }

    @PreAuthorize("@articleSecurityInspector.isThisWriter(#cmd.articleId)")
    public void modify(ModifyArticleCommand cmd) {
        Article article = articleConstraint.checkExistsAndGet(cmd.getArticleId());
        article.modify(cmd.getTitle(), cmd.getBody(), cmd.getTags());
    }

    public void move(MoveArticleCommand cmd) {
        Article article = articleConstraint.checkExistsAndGet(cmd.getArticleId());
        article.changeBoard(cmd.getBoardId());
    }

    public void increaseHitCount(String articleId) {
        Article article = articleConstraint.checkExistsAndGet(articleId);
        article.increaseHitCount();
    }

    public void toggleScrap(String articleId, String scrapperId) {
        ArticleScrap scrap = articleScrapRepository.find(articleId, scrapperId).orElse(null);
        if (scrap == null)
            scrap(articleId, scrapperId);
        else
            unscrap(scrap);
    }

    public void toggleBlock(String articleId) {
        Article article = articleConstraint.checkExistsAndGet(articleId);
        article.toggleBlock();
    }

    public void toggleChoice(String articleId) {
        Article article = articleConstraint.checkExistsAndGet(articleId);
        article.toggleChoice();
    }

    @PreAuthorize("@articleSecurityInspector.isThisWriter(#articleId)")
    public void remove(String articleId) {
        articleConstraint.rejectRemoveIfHasReplies(articleId);
        Article article = articleConstraint.checkExistsAndGet(articleId);
        article.markDelete();
    }

    public void removeForce(String articleId) {
        Article article = articleConstraint.checkExistsAndGet(articleId);
        article.markDeleteForce();
    }

    // ------------------------------------------
    private void scrap(String articleId, String scrapperId) {
        articleConstraint.checkExists(articleId);
        ArticleScrap scrap = new ArticleScrap(articleId, scrapperId);
        articleScrapRepository.save(scrap);
        DomainEventPublisher.fire(mapper.toEvent(scrap));
    }

    private void unscrap(ArticleScrap scrap) {
        articleScrapRepository.delete(scrap);
    }
}
