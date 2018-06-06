package org.okky.article.domain.model;

import org.junit.Test;
import org.okky.article.TestMother;
import org.okky.share.execption.ModelConflicted;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArticleStatusTest extends TestMother {
    @Test
    public void blocked_토글_확인() {
        Article article = fixture();
        assertFalse(article.blocked());

        article.toggleBlock();
        assertTrue(article.blocked());

        article.toggleBlock();
        assertFalse(article.blocked());
    }

    @Test
    public void choice_토글_확인() {
        Article article = fixture();
        assertFalse(article.choosed());

        article.toggleChoice();
        assertTrue(article.choosed());

        article.toggleChoice();
        assertFalse(article.choosed());
    }

    @Test
    public void blocked면_수정할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.toggleBlock();
        article.modify("제목1", "바디1", null);
    }

    @Test
    public void blocked면_게시판_바꿀_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.toggleBlock();
        article.changeBoard("notice");
    }

    @Test
    public void deleted면_수정할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.modify("제목1", "바디1", null);
    }

    @Test
    public void deleted면_게시판_바꿀_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.changeBoard("notice");
    }

    @Test
    public void deleted면_조회수_증가할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.increaseHitCount();
    }

    @Test
    public void deleted면_토글_블락_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.toggleBlock();
    }

    @Test
    public void deleted면_토글_초이스_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.toggleChoice();
    }

    @Test
    public void deleted면_다시_삭제할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.markDelete();
    }

    @Test
    public void deleted면_강제삭제할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDelete();
        article.markDeleteForce();
    }

    @Test
    public void deletedForce면_수정할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.modify("제목1", "바디1", null);
    }

    @Test
    public void deletedForce면_게시판_바꿀_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.changeBoard("notice");
    }

    @Test
    public void deletedForce면_조회수_증가할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.increaseHitCount();
    }

    @Test
    public void deletedForce면_토글_블락_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.toggleBlock();
    }

    @Test
    public void deletedForce면_토글_초이스_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.toggleChoice();
    }

    @Test
    public void deletedForce면_다시_삭제할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.markDelete();
    }

    @Test
    public void deletedForce면_강제삭제할_때_예외() {
        expect(ModelConflicted.class);

        Article article = fixture();
        article.markDeleteForce();
        article.markDeleteForce();
    }

    // -----------------------------------
    private Article fixture() {
        List<String> tags = new ArrayList<>();
        tags.add("ddd");
        return new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", tags);
    }
}