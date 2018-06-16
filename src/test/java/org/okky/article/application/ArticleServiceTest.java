package org.okky.article.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.ArticleScrapRepository;
import org.okky.article.domain.service.ArticleConstraint;
import org.okky.article.domain.service.BoardConstraint;
import org.okky.article.domain.service.ServiceTestMother;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.mock;

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

    @Test
    public void write() {
        WriteArticleCommand cmd = mock(WriteArticleCommand.class);

        service.write(cmd);
    }

    @Test
    public void toggleScrap() {
    }
}