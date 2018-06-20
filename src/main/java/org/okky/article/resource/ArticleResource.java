package org.okky.article.resource;

import lombok.AllArgsConstructor;
import org.okky.article.application.ArticleService;
import org.okky.article.application.command.ModifyArticleCommand;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.repository.ArticleMapper;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.dto.ArticleDto;
import org.okky.share.PagingEnvelop;
import org.okky.share.execption.ResourceNotFound;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
class ArticleResource {
    ArticleService service;
    ArticleMapper mapper;
    ArticleRepository repository;
    ContextHolder holder;

    @GetMapping(value = "/articles", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getArticles(
            @RequestParam(required = false) String boardId,
            @RequestParam(required = false) String writerId,
            @RequestParam(required = false) Boolean choice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "wroteOn") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("writerId", writerId);
        params.put("choice", choice);
        params.put("search", search);
        params.put("sort", sort);
        params.put("order", order);
        params.put("offset", (pageNo - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("excludeDeleted", true);

        List<ArticleDto> dtos = mapper.select(params);
        long totalCount = mapper.count(params);
        return new PagingEnvelop(pageSize, dtos, totalCount);
    }

    @GetMapping(value = "/boards/{boardId}/articles", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getArticles(
            @PathVariable(required = false) String boardId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return getArticles(boardId, null, null, search, "wroteOn", "desc", pageNo, pageSize);
    }

    @GetMapping(value = "/boards/best/articles", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getMyBestArticles(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return getArticles(null, null, null, search, "hitCount", "desc", pageNo, pageSize);
    }

    @GetMapping(value = "/boards/choice/articles", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getMyChoiceArticles(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return getArticles(null, null, true, search, "choosedOn", "desc", pageNo, pageSize);
    }

    @GetMapping(value = "/members/me/articles", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getMyWroteArticles(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "wroteOn") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return getArticles(null, holder.getId(), null, search, sort, order, pageNo, pageSize);
    }

    @GetMapping(value = "/members/me/articles/scrapped", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getMyScrappedArticles(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("scrapperId", holder.getId());
        params.put("search", search);
        params.put("offset", (pageNo - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("myId", holder.getId());

        List<ArticleDto> dtos = mapper.selectFromScrap(params);
        long totalCount = mapper.countFromScrap(params);
        return new PagingEnvelop(pageSize, dtos, totalCount);
    }

    @GetMapping(value = "/articles/tags/{tag}", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getByTags(
            @PathVariable String tag,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("tag", tag);
        params.put("search", search);
        params.put("offset", (pageNo - 1) * pageSize);
        params.put("limit", pageSize);
        List<ArticleDto> dtos = mapper.selectFromTag(params);
        long totalCount = mapper.countFromTag(params);
        return new PagingEnvelop(pageSize, dtos, totalCount);
    }

    @GetMapping(value = "/articles/{articleId}", produces = APPLICATION_JSON_VALUE)
    ArticleDto get(@PathVariable String articleId) {
        return mapper.selectOne(articleId, holder.getId());
    }

    @GetMapping(value = "/articles/{articleId}/exists")
    boolean exists(@PathVariable String articleId) {
        return repository.existsById(articleId);
    }

    @GetMapping(value = "/articles/{articleId}/blocked")
    boolean blocked(@PathVariable String articleId) {
        return repository
                .findById(articleId)
                .orElseThrow(ResourceNotFound::new)
                .blocked();
    }

    @GetMapping(value = "/members/{memberId}/articles/count")
    long countMyArticles(@PathVariable String memberId) {
        return repository.countByWriterId(memberId);
    }

    @PostMapping(value = "/boards/{boardId}/articles", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    void write(
            @PathVariable String boardId,
            @RequestBody WriteArticleCommand cmd) {
        cmd.setBoardId(boardId);
        cmd.setWriterId(holder.getId());
        service.write(cmd);
    }

    @PostMapping(value = "/articles/{articleId}/hit-count/increase")
    @ResponseStatus(NO_CONTENT)
    void increaseHitCount(@PathVariable String articleId) {
        service.increaseHitCount(articleId);
    }

    @PutMapping(value = "/articles/{articleId}/scraps/toggle")
    long toggleScrap(
            @PathVariable String articleId) {
        service.toggleScrap(articleId, holder.getId());
        return mapper.countScrapByArticleId(articleId);
    }

    @PutMapping(value = "/articles/{articleId}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    void update(@PathVariable String articleId, @RequestBody ModifyArticleCommand cmd) {
        cmd.setArticleId(articleId);
        service.modify(cmd);
    }

    @DeleteMapping(value = "/articles/{articleId}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable String articleId) {
        service.remove(articleId);
    }
}
