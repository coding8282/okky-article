package org.okky.article.domain.repository;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.okky.article.domain.model.ArticleScrap;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void saveAndFind_없을_때() {
        boolean present = repository.find("a-1", "s-1").isPresent();

        assertFalse("없을 때는 false를 반환해야 한다.", present);
    }

    // ---------------------------------------------------
    private ArticleScrap fixture() {
        return ArticleScrap.sample();
    }
}