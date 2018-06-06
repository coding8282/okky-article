package org.okky.article.domain.model;

import org.junit.Test;
import org.okky.article.TestMother;
import org.okky.share.execption.BadArgument;

import java.util.List;

import static java.util.Arrays.asList;

public class TagRuleTest extends TestMother {
    @Test
    public void 태그가_0개라면_예외() {
        expect(BadArgument.class, "태그 개수는 최소 1개 ~ 최대 7개까지만 가능합니다.");

        List<String> tags = asList();
        TagRule.rejectIfEmptyOrTooMany(tags);
    }

    @Test
    public void 태그가_8개라면_예외() {
        expect(BadArgument.class, "태그 개수는 최소 1개 ~ 최대 7개까지만 가능합니다.");

        List<String> tags = asList();
        TagRule.rejectIfEmptyOrTooMany(tags);
    }

    @Test
    public void 태그에_대쉬가_들어간_경우_예외() {
        expect(BadArgument.class, "태그는 _을 제외한 특수문자를 포함할 수 없습니다: 'Java-Programming'");

        List<String> tags = asList("Java-Programming");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그에_슬래쉬가_들어간_경우_예외() {
        expect(BadArgument.class, "태그는 _을 제외한 특수문자를 포함할 수 없습니다: 'Java/Programming'");

        List<String> tags = asList("Java/Programming");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그에_밑줄이_들어간_경우_OK() {
        List<String> tags = asList("Java_Programming");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그에_공백이_들어간_경우_예외() {
        expect(BadArgument.class);

        List<String> tags = asList("Java is king");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그에_욕이_들어간_경우_예외() {
        expect(BadArgument.class, "해당 태그에는 금지된 단어('씨발')가 포함되어 있습니다.");

        List<String> tags = asList("공지사항", "씨발");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그가_0글자라면_예외() {
        expect(BadArgument.class);

        List<String> tags = asList("");
        TagRule.rejectIfIllegalNames(tags);
    }

    @Test
    public void 태그가_21글자라면_예외() {
        expect(BadArgument.class);

        List<String> tags = asList("123456789012345678901");
        TagRule.rejectIfIllegalNames(tags);
    }


    @Test
    public void 태그가_1글자는_허용() {
        List<String> tags = asList("A");
        TagRule.rejectIfIllegalNames(tags);
    }
}