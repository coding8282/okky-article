package org.okky.article.domain.model;

import org.junit.Test;
import org.okky.article.TestMother;
import org.okky.share.execption.BadArgument;

public class ArticleScrapTest extends TestMother {
    @Test
    public void new_articleId는_필수() {
        expect(BadArgument.class, "게시글 id는 필수입니다.");

        new ArticleScrap(null, "m-30004");
    }

    @Test
    public void new_scrapperId는_필수() {
        expect(BadArgument.class, "scrapper id는 필수입니다.");

        new ArticleScrap("a-11", null);
    }
}