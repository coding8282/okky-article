package org.okky.article.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.domain.Aggregate;
import org.okky.share.domain.AssertionConcern;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@FieldDefaults(level = PRIVATE)
@Getter
@Entity
public class ArticleScrap implements Aggregate {
    @Id
    @Column(length = 50)
    String id;

    @Column(nullable = false, length = 50)
    String articleId;

    @Column(nullable = false)
    String scrapperId;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    long scrappedOn;

    public ArticleScrap(String articleId, String scrapperId) {
        setId("as-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12));
        setArticleId(articleId);
        setScrapperId(scrapperId);
    }

    // -----------------------------------------------------
    public static ArticleScrap sample() {
        String articleId = "article-id";
        String scrapperId = "m-30004";
        return new ArticleScrap(articleId, scrapperId);
    }

    public static void main(String[] args) {
        System.out.println(sample());
    }

    private void setId(String id) {
        AssertionConcern.assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setArticleId(String articleId) {
        AssertionConcern.assertArgNotNull(articleId, "게시글 id는 필수입니다.");
        this.articleId = articleId;
    }

    private void setScrapperId(String scrapperId) {
        AssertionConcern.assertArgNotNull(scrapperId, "scrapper id는 필수입니다.");
        this.scrapperId = scrapperId;
    }
}
