package org.okky.article.domain.model;

import org.junit.Test;
import org.okky.article.TestMother;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TagRefactorTest extends TestMother {
    @Test
    public void null은_모두_제거() {
        List<String> tags = asList(null, null, "Spring", null);
        List<String> refactored = TagRefactor.refactor(tags);

        assertThat(refactored.get(0), is("spring"));
    }

    @Test
    public void 화이트스페이스문자열은_모두_제거() {
        List<String> tags = asList("\t", "", "Spring", "\n");
        List<String> refactored = TagRefactor.refactor(tags);

        assertThat(refactored.get(0), is("spring"));
    }

    @Test
    public void 공백이_있는_경우_trim() {
        List<String> tags = asList("\n빨리빨리코딩yo9\t ");
        List<String> refactored = TagRefactor.refactor(tags);

        assertThat(refactored.get(0), is("빨리빨리코딩yo9"));
    }

    @Test
    public void 영어는_항상_소문자로() {
        List<String> tags = asList("CoDing");
        List<String> refactored = TagRefactor.refactor(tags);

        assertThat(refactored.get(0), is("coding"));
    }
}