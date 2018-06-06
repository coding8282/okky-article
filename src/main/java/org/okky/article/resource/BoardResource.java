package org.okky.article.resource;

import lombok.AllArgsConstructor;
import org.okky.article.application.BoardService;
import org.okky.article.application.command.CreateBoardCommand;
import org.okky.article.application.command.ModifyBoardCommand;
import org.okky.article.domain.model.Board;
import org.okky.article.domain.repository.BoardRepository;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
class BoardResource {
    BoardService service;
    BoardRepository repository;

    @GetMapping(value = "/boards")
    Iterable<Board> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/boards/{boardId}", produces = APPLICATION_JSON_VALUE)
    Board getOne(@PathVariable String boardId) {
        return repository.findById(boardId).orElse(null);
    }

    @PostMapping(value = "/boards", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    void create(@RequestBody CreateBoardCommand cmd) {
        service.create(cmd);
    }

    @PutMapping(value = "/boards/{boardId}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    void update(@PathVariable String boardId, @RequestBody ModifyBoardCommand cmd) {
        cmd.setId(boardId);
        service.modify(cmd);
    }

    @DeleteMapping(value = "/boards/{boardId}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable String boardId) {
        service.remove(boardId);
    }
}
