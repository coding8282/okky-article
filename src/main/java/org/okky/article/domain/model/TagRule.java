package org.okky.article.domain.model;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.okky.share.execption.BadArgument;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.okky.share.domain.AssertionConcern.assertArgLength;

@NoArgsConstructor(access = PRIVATE)
public final class TagRule {
    public static final List<String> BAD_TAGS = asList("fuck,시발,씨발,좆,새끼,병신".split(","));
    static final int MIN_COUNT = 1;
    static final int MAX_COUNT = 7;
    static final int MIN_NAME_LEN = 1;
    static final int MAX_NAME_LEN = 20;

    public static void rejectIfEmptyOrTooMany(List<String> tags) {
        int tagCount = tags.size();
        if (!(MIN_COUNT <= tagCount && tagCount <= MAX_COUNT))
            throw new BadArgument(format("태그 개수는 최소 %,d개 ~ 최대 %,d개까지만 가능합니다.", MIN_COUNT, MAX_COUNT));
    }

    public static void rejectIfIllegalNames(List<String> tags) {
        tags.forEach(TagRule::rejectIfIllegalName);
    }

    // --------------------------------
    private static void rejectIfIllegalName(String tag) {
        // 길이 검사
        assertArgLength(tag, MIN_NAME_LEN, MAX_NAME_LEN, format("태그는 %d~%d자까지 가능합니다.", MIN_NAME_LEN, MAX_NAME_LEN));

        // _을 제외한 특문/구두점 검사
        if (!StringUtils.isAlphaSpace(tag.replaceAll("_", " ")))
            throw new BadArgument(format("태그는 _을 제외한 특수문자를 포함할 수 없습니다: '%s'", tag));

        // 공백문자 검사
        if (StringUtils.containsWhitespace(tag))
            throw new BadArgument(format("태그는 공백문자를 포함할 수 없습니다: '%s'", tag));

        // 욕설 검사
        boolean isBad = BAD_TAGS
                .stream()
                .anyMatch(badTag -> containsIgnoreCase(tag, badTag));
        if (isBad)
            throw new BadArgument(format("해당 태그에는 금지된 단어('%s')가 포함되어 있습니다.", tag));
    }
}
