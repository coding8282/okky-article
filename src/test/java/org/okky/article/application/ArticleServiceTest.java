package org.okky.article.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.model.Article;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.ArticleScrapRepository;
import org.okky.article.domain.service.ArticleConstraint;
import org.okky.article.domain.service.BoardConstraint;
import org.okky.article.domain.service.ServiceTestMother;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@FieldDefaults(level = PRIVATE)
public class ArticleServiceTest extends ServiceTestMother {
    @InjectMocks
    ArticleService service;
    @Mock
    ArticleRepository articleRepository;
    @Mock
    ArticleScrapRepository articleScrapRepository;
    @Mock
    BoardConstraint boardConstraint;
    @Mock
    ArticleConstraint articleConstraint;
    @Mock
    ModelMapper mapper;
    @Mock
    Article article;

    @Test
    public void write() {
        WriteArticleCommand cmd = new WriteArticleCommand("b-1", "1", "2", "3", "4", null);
        when(mapper.toModel(cmd)).thenReturn(article);
        when(article.getId()).thenReturn("a-1234");

        String id = service.write(cmd);
        assertEquals("게시글이 성공적으로 생성되었으므로 아이디를 반환해야 한다.", "a-1234", id);

        InOrder o = inOrder(boardConstraint, mapper, articleRepository);
        o.verify(boardConstraint).checkExists("b-1");
        o.verify(mapper).toModel(cmd);
        o.verify(articleRepository).save(article);
    }

    @Test
    public void remove() {
        Article article = mock(Article.class);
        when(articleConstraint.checkExistsAndGet("a-1")).thenReturn(article);

        service.remove("a-1");

        InOrder o = inOrder(articleConstraint, article);
        o.verify(articleConstraint).rejectRemoveIfHasReplies("a-1");
        o.verify(articleConstraint).checkExistsAndGet("a-1");
        o.verify(article).markDelete();
    }
}