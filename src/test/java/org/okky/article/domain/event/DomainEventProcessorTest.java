package org.okky.article.domain.event;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.article.TestMother;
import org.okky.article.domain.service.ArticleProxy;
import org.okky.share.event.ArticleScrapped;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class DomainEventProcessorTest extends TestMother {
    @InjectMocks
    DomainEventProcessor processor;
    @Mock
    ArticleProxy proxy;

    @Test
    public void when_ArticleScrapped() {
        ArticleScrapped event = mock(ArticleScrapped.class);

        processor.when(event);

        InOrder o = inOrder(proxy);
        o.verify(proxy).sendEvent(event);
    }
}