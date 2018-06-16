package org.okky.article.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.okky.article.application.command.CreateBoardCommand;
import org.okky.article.application.command.ModifyBoardCommand;
import org.okky.article.domain.model.Board;
import org.okky.article.domain.repository.BoardRepository;
import org.okky.article.domain.service.BoardConstraint;
import org.okky.article.domain.service.ServiceTestMother;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@FieldDefaults(level = PRIVATE)
public class BoardServiceTest extends ServiceTestMother {
    @InjectMocks
    BoardService service;
    @Mock
    BoardRepository repository;
    @Mock
    BoardConstraint constraint;
    @Mock
    ModelMapper mapper;
    @Mock
    Board board;

    @Test
    public void create() {
        CreateBoardCommand cmd = new CreateBoardCommand("1", "공지사항", "3", "4");
        when(mapper.toModel(cmd)).thenReturn(board);

        service.create(cmd);

        InOrder o = inOrder(constraint, mapper, repository);
        o.verify(constraint).checkUniqueName("공지사항");
        o.verify(mapper).toModel(cmd);
        o.verify(repository).save(board);
    }

    @Test
    public void modify_이름이_다를_때() {
        ModifyBoardCommand cmd = new ModifyBoardCommand("b-1", "공지사항", "3", "4");
        when(constraint.checkExistsAndGet("b-1")).thenReturn(board);
        when(board.isDifferentName("공지사항")).thenReturn(true);

        service.modify(cmd);

        InOrder o = inOrder(constraint, board);
        o.verify(constraint).checkUniqueName("공지사항");
        o.verify(board).modify("공지사항", "3", "4");
    }

    @Test
    public void modify_이름이_같을_때() {
        ModifyBoardCommand cmd = new ModifyBoardCommand("b-1", "공지사항", "3", "4");
        when(constraint.checkExistsAndGet("b-1")).thenReturn(board);
        when(board.isDifferentName("공지사항")).thenReturn(false);

        service.modify(cmd);

        InOrder o = inOrder(constraint, board);
        o.verify(board).modify("공지사항", "3", "4");
    }

    @Test
    public void remove() {
        when(constraint.checkExistsAndGet("b-1")).thenReturn(board);

        service.remove("b-1");

        InOrder o = inOrder(constraint, repository);
        o.verify(constraint).rejectRemoveIfHasArticles("b-1");
        o.verify(constraint).checkExistsAndGet("b-1");
        o.verify(repository).delete(board);
    }
}