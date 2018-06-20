package org.okky.article.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.domain.Aggregate;

import javax.persistence.*;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@FieldDefaults(level = PRIVATE)
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "U_ARTICLE_ID_SCRAPPER_ID",
                columnNames = {"ARTICLE_ID", "SCRAPPER_ID",}
        )
})
public class ArticleScrap implements Aggregate {
    @Id
    @Column(length = 50)
    String id;

    @Column(name = "ARTICLE_ID", nullable = false, length = 50)
    String articleId;

    @Column(name = "SCRAPPER_ID", nullable = false)
    String scrapperId;

    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    long scrappedOn;

    public ArticleScrap(String articleId, String scrapperId) {
        setId("as-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12));
        setArticleId(articleId);
        setScrapperId(scrapperId);
        setScrappedOn(currentTimeMillis());
    }

    public static ArticleScrap sample() {
        String articleId = "article-id";
        String scrapperId = "m-30004";
        return new ArticleScrap(articleId, scrapperId);
    }

    public static void main(String[] args) {
        System.out.println(sample());
    }

    // -----------------------------------------------------
    private void setId(String id) {
        assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setArticleId(String articleId) {
        assertArgNotNull(articleId, "게시글 id는 필수입니다.");
        this.articleId = articleId;
    }

    private void setScrapperId(String scrapperId) {
        assertArgNotNull(scrapperId, "scrapper id는 필수입니다.");
        this.scrapperId = scrapperId;
    }

    private void setScrappedOn(long scrappedOn) {
        this.scrappedOn = scrappedOn;
    }
}
