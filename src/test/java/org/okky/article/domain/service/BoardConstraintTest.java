package org.okky.article.domain.service;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.article.TestMother;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.BoardRepository;
import org.okky.share.execption.ModelConflicted;
import org.okky.share.execption.ModelNotExists;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class BoardConstraintTest extends TestMother {
    @InjectMocks
    BoardConstraint constraint;
    @Mock
    BoardRepository boardRepository;
    @Mock
    ArticleRepository articleRepository;

    @Test
    public void checkExistsAndGet_게시판이_없는_경우_예외() {
        expect(ModelNotExists.class, "해당 게시판은 존재하지 않습니다: 'b-1'");

        when(boardRepository.findById("b-1")).thenReturn(Optional.empty());

        constraint.checkExistsAndGet("b-1");
    }

    @Test
    public void checkUniqueName_같은_이름이_있는_경우_예외() {
        expect(ModelConflicted.class, "같은 명칭의 게시판이 이미 존재합니다: 'b-1'");

        when(boardRepository.existsByName("b-1")).thenReturn(true);

        constraint.checkUniqueName("b-1");
    }

    @Test
    public void rejectRemoveIfHasArticles_게시글이_존재하는_경우_예외() {
        expect(ModelConflicted.class, "게시판에 게시글이 존재하기 때문에 삭제할 수 없습니다: 'b-1'");

        when(articleRepository.countByBoardId("b-1")).thenReturn(3L);

        constraint.rejectRemoveIfHasArticles("b-1");
    }
}