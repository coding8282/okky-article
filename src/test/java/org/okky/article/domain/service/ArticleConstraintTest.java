package org.okky.article.domain.service;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.article.TestMother;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.share.execption.ModelNotExists;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class ArticleConstraintTest extends TestMother {
    @InjectMocks
    ArticleConstraint constraint;
    @Mock
    ArticleRepository repository;

    @Test
    public void checkExistsAndGet_없는_아이디인_경우_예외() {
        expect(ModelNotExists.class, "해당 게시글은 존재하지 않습니다: 'a1'");

        when(repository.findById("a1")).thenReturn(Optional.empty());

        constraint.checkExistsAndGet("a1");
    }
}