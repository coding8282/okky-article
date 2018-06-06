package org.okky.article.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface TagRefactor {
    /**
     * null제거/화이트스페이스문자열제거/trim/소문자로
     */
    static List<String> refactor(List<String> tags) {
        return tags
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> !StringUtils.isWhitespace(x))
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }
}
