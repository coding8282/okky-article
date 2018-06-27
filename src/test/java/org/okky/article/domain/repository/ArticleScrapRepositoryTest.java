package org.okky.article.domain.repository;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.okky.article.domain.model.ArticleScrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@FieldDefaults(level = PRIVATE)
public class ArticleScrapRepositoryTest extends RepositoryTestMother {
    @Autowired
    ArticleScrapRepository repository;

    @Test
    public void saveAndFind() {
        ArticleScrap scrap = fixture();
        repository.save(scrap);
        ArticleScrap found = repository.find(scrap.getArticleId(), scrap.getScrapperId()).get();

        assertThat("아이디가 일치하지 않는다.", scrap.getId(), is(found.getId()));
    }

    @Test
    public void saveAndFind_없을_때_확인() {
        boolean present = repository.find("a-1", "s-1").isPresent();

        assertFalse("없을 때는 false를 반환해야 한다.", present);
    }

    @Test
    public void articleId_scrapperId_유니크_제약조건_확인() {
        expect(DataIntegrityViolationException.class);

        repository.saveAndFlush(fixture());
        repository.saveAndFlush(fixture());
    }

    // ---------------------------------------------------
    private ArticleScrap fixture() {
        String articleId = "article-id";
        String scrapperId = "m-30004";
        return new ArticleScrap(articleId, scrapperId);
    }
}