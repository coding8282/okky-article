package org.okky.article.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.okky.article.TestMother;
import org.okky.article.domain.model.ArticleScrap;
import org.okky.share.event.ArticleScrapped;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@FieldDefaults(level = PRIVATE)
public class ModelMapperTest extends TestMother {
    ModelMapper mapper = new ModelMapper();

    @Test
    public void toEvent() {
        ArticleScrap scrap = fixture();
        ArticleScrapped event = mapper.toEvent(scrap);

        assertThat("id가 다르다", event.getId(), is(scrap.getId()));
        assertThat("articleId가 다르다", event.getArticleId(), is(scrap.getArticleId()));
        assertThat("scrapperId가 다르다", event.getScrapperId(), is(scrap.getScrapperId()));
        assertThat("scrappedOn이 다르다", event.getScrappedOn(), is(scrap.getScrappedOn()));
    }

    ArticleScrap fixture() {
        String articleId = "article-id";
        String scrapperId = "m-30004";
        return new ArticleScrap(articleId, scrapperId);
    }
}