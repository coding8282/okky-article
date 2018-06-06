package org.okky.article.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.okky.article.domain.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository repository;

    @Test
    public void findById() {
        Article article = repository.findById("111").orElse(null);
        assertThat(article).isNull();
    }
}