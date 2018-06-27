package org.okky.article.application;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.article.application.command.CreateBoardCommand;
import org.okky.article.application.command.ModifyBoardCommand;
import org.okky.article.domain.model.Board;
import org.okky.article.domain.repository.BoardRepository;
import org.okky.article.domain.service.BoardConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class BoardService {
    BoardRepository repository;
    BoardConstraint constraint;
    ModelMapper mapper;

    public void create(CreateBoardCommand cmd) {
        constraint.checkUniqueName(cmd.getName());
        Board board = mapper.toModel(cmd);
        repository.save(board);
    }

    public void modify(ModifyBoardCommand cmd) {
        String name = cmd.getName();
        Board board = constraint.checkExistsAndGet(cmd.getId());
        if (board.isDifferentName(name))
            constraint.checkUniqueName(name);
        board.modify(name, cmd.getShortDescription(), cmd.getDescription());
    }

    public void remove(String boardId) {
        constraint.rejectRemoveIfHasArticles(boardId);
        Board board = constraint.checkExistsAndGet(boardId);
        repository.delete(board);
    }
}
