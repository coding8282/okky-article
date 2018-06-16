package org.okky.article.domain.repository;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okky.article.domain.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@FieldDefaults(level = PRIVATE)
public class ArticleRepositoryTest {
    @Autowired
    ArticleRepository repository;

    @Test
    public void saveAndFindById() {
        Article article = fixture();
        repository.save(article);
        Article found = repository.findById(article.getId()).get();

        assertThat("아이디가 일치하지 않는다.", article.getId(), is(found.getId()));
    }

    @Test
    public void saveAndFindById_없을_때() {
        boolean present = repository.findById("a-34343434").isPresent();

        assertFalse("없을 때는 false를 반환해야 한다.", present);
    }

    @Test
    public void saveAndExistsById() {
        Article article = fixture();
        repository.save(article);
        boolean exists = repository.existsById(article.getId());

        assertTrue("저장했지만 존재하지 않는다.", exists);
    }

    @Test
    public void countByWriterId() {
        repository.save(withWriterId("id-1"));
        repository.save(withWriterId("id-1"));
        repository.save(withWriterId("id-1"));
        repository.save(withWriterId("id-5"));
        long count = repository.countByWriterId("id-1");

        assertThat("3개가 나와야 한다.", count, is(3L));
    }

    @Test
    public void countByBoardId() {
        repository.save(withBoardId("b-1"));
        repository.save(withBoardId("b-2"));
        repository.save(withBoardId("b-2"));
        repository.save(withBoardId("b-3"));
        long count = repository.countByBoardId("b-2");

        assertThat("2개가 나와야 한다.", count, is(2L));
    }

    @Test
    public void countByBoardId_못_찾았을_때는_0_반환() {
        repository.save(withBoardId("b-1"));
        long count = repository.countByBoardId("b-100");

        assertThat("2개가 나와야 한다.", count, is(0L));
    }

    // ---------------------------
    private Article fixture() {
        return new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", asList("java"));
    }

    private Article withWriterId(String writerId) {
        return new Article("bid", "제목입니다", "에러 스택 좀 봐주세용...", writerId, "뚜뚜", asList("java"));
    }

    private Article withBoardId(String boardId) {
        return new Article(boardId, "제목입니다", "에러 스택 좀 봐주세용...", "coding8282", "뚜뚜", asList("java"));
    }
}