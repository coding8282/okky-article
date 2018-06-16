package org.okky.article.resource;

import lombok.AllArgsConstructor;
import org.okky.article.application.ArticleService;
import org.okky.article.application.command.MoveArticleCommand;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.repository.ArticleMapper;
import org.okky.article.domain.repository.ArticleRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Secured("ROLE_ADMIN")
@RestController
@AllArgsConstructor
class ArticleResourceAdmin {
    ArticleService service;
    ArticleMapper mapper;
    ArticleRepository repository;

    @PostMapping(value = "/boards/notice/articles", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    void writeNotice(@RequestBody WriteArticleCommand cmd) {
        cmd.setBoardId("notice");
        cmd.setWriterId(ContextHelper.getId());
        service.write(cmd);
    }

    @PutMapping(value = "/articles/{articleId}/choices/toggle")
    @ResponseStatus(NO_CONTENT)
    void toggleChoice(@PathVariable String articleId) {
        service.toggleChoice(articleId);
    }

    @PutMapping(value = "/articles/{articleId}/blocks/toggle")
    @ResponseStatus(NO_CONTENT)
    void toggleBlock(@PathVariable String articleId) {
        service.toggleBlock(articleId);
    }

    @PutMapping(value = "/articles/{articleId}/move")
    @ResponseStatus(NO_CONTENT)
    void move(@PathVariable String articleId, @RequestParam("id") String boardId) {
        MoveArticleCommand cmd = new MoveArticleCommand(articleId, boardId);
        service.move(cmd);
    }

    @DeleteMapping(value = "/articles/{articleId}/force")
    @ResponseStatus(NO_CONTENT)
    void deleteForce(@PathVariable String articleId) {
        service.removeForce(articleId);
    }
}
