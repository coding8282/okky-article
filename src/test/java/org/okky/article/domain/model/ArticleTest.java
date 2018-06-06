package org.okky.article.domain.model;

import org.junit.Test;
import org.okky.article.TestMother;
import org.okky.share.execption.BadArgument;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ArticleTest extends TestMother {
    @Test
    public void 태그에_null만_있는_경우_예외() {
        expect(BadArgument.class, "태그 개수는 최소 1개 ~ 최대 7개까지만 가능합니다.");

        List<String> newTags = new ArrayList<>();
        newTags.add(null);
        newTags.add(null);
        new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", newTags);
    }

    @Test
    public void 태그가_없는_경우() {
        expect(BadArgument.class, "태그 개수는 최소 1개 ~ 최대 7개까지만 가능합니다.");

        List<String> newTags = new ArrayList<>();
        new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", newTags);
    }

    @Test
    public void 태그는_모델을_통해서_수정하지_않으면_예외() {
        expect(UnsupportedOperationException.class);

        List<String> newTags = asList("Java");
        Article article = new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", newTags);
        List<String> tags = article.getTags();
        tags.add("DDD");
    }

    @Test
    public void 태그는_null을_제외_화이트스페이스문자열을_제외_trim하며_소문자로_변환하고_순서를_유지하며_중복을_제거() {
        List<String> newTags = asList(null, "", "\tJava", "JAVA", "Spring", "AOP", "SPRing", "메롱", "\t\n");
        Article article = new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", newTags);
        List<String> tags = article.getTags();

        assertThat(tags, hasSize(4));
        assertThat(tags.get(0), is("java"));
        assertThat(tags.get(1), is("spring"));
        assertThat(tags.get(2), is("aop"));
        assertThat(tags.get(3), is("메롱"));
    }

    @Test
    public void 조회수_증가_확인() {
        Article article = fixture();
        assertThat(article.getHitCount(), is(0L));

        article.increaseHitCount();
        article.increaseHitCount();
        article.increaseHitCount();
        assertThat(article.getHitCount(), is(3L));
    }

    @Test
    public void 수정_확인() {
        Article article = fixture();
        article.modify("제목2", "내용12345", asList("spring"));

        assertThat(article.getTitle(), is("제목2"));
        assertThat(article.getBody(), is("내용12345"));
        assertThat(article.getTags(), is(asList("spring")));
    }

    @Test
    public void 게시판_이동_확인() {
        Article article = fixture();
        article.changeBoard("b-9");

        assertThat(article.getBoardId(), is("b-9"));
    }

    // -----------------------------------
    private Article fixture() {
        List<String> tags = new ArrayList<>();
        tags.add("ddd");
        return new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", tags);
    }
}