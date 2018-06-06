package org.okky.article.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.okky.share.domain.Aggregate;
import org.okky.share.execption.ModelConflicted;
import org.okky.share.util.JsonUtil;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.article.domain.model.ArticleStatus.*;
import static org.okky.share.domain.AssertionConcern.*;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ARTICLE",
        indexes = {
                @Index(name = "I_BOARD_ID", columnList = "BOARD_ID")
        }
)
@Audited
public class Article implements Aggregate {
    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "BOARD_ID", nullable = false, length = 50)
    private String boardId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 3000)
    private String body;

    @Column(nullable = false)
    @NotAudited
    private String writerId;

    @Column(nullable = false)
    private String writerName;

    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private long hitCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "NAME", nullable = false)
    @CollectionTable(
            name = "ARTICLE_TAG",
            joinColumns = @JoinColumn(name = "ARTICLE_ID")
    )
    @OrderColumn(name = "IDX", columnDefinition = "BIGINT UNSIGNED")
    @NotAudited
    private List<String> tags;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @NotAudited
    private long wroteOn;

    @LastModifiedDate
    @Column
    private long updatedOn;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    @NotAudited
    private Long choosedOn;

    @Enumerated(STRING)
    @Column(nullable = false, length = 30)
    @NotAudited
    private ArticleStatus status;

    public Article(String boardId, String title, String body, String writerId, String writerName, List<String> tags) {
        setId("a-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setBoardId(boardId);
        setTitle(title);
        setBody(body);
        setWriterId(writerId);
        setWriterName(writerName);
        setHitCount(0L);
        setTags(tags);
        setStatus(NORMAL);
    }

    // ---------------------------------
    public static Article sample() {
        String boardId = "bid";
        String title = "제목입니다";
        String body = "에러 스택 좀 봐주세용...";
        String writerId = "coding8282";
        String writerName = "뚜뚜";
        List<String> tags = new ArrayList<>();
        tags.add("Java");// 태그는 일부러 중복
        tags.add("Java");
        tags.add("Spring");
        tags.add("AOP");
        tags.add("Spring");
        return new Article(boardId, title, body, writerId, writerName, tags);
    }

    public static void main(String[] args) {
        System.out.println(JsonUtil.toPrettyJson(sample()));
    }

    public void modify(String title, String body, List<String> tags) {
        checkBlocked();
        checkDeleted();
        checkDeletedForce();
        setTitle(title);
        setBody(body);
        setTags(tags);
    }

    public void changeBoard(String newBoardId) {
        checkBlocked();
        checkDeleted();
        checkDeletedForce();
        setBoardId(newBoardId);
    }

    public void increaseHitCount() {
        checkDeleted();
        checkDeletedForce();
        setHitCount(hitCount + 1);
    }

    public void toggleBlock() {
        checkDeleted();
        checkDeletedForce();
        if (blocked())
            setStatus(NORMAL);
        else
            setStatus(BLOCKED);
    }

    public void toggleChoice() {
        checkDeleted();
        checkDeletedForce();
        if (choosed())
            choosedOn = null;
        else
            choosedOn = currentTimeMillis();
    }

    public void markDelete() {
        checkBlocked();
        checkDeleted();
        checkDeletedForce();
        setStatus(DELETED);
    }

    public void markDeleteForce() {
        checkDeleted();
        checkDeletedForce();

        setStatus(DELETED_FORCE);
    }

    public boolean blocked() {
        return status == BLOCKED;
    }

    public boolean choosed() {
        return choosedOn != null;
    }

    public boolean deleted() {
        return status == DELETED;
    }

    public boolean deletedForce() {
        return status == DELETED_FORCE;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    // ---------------------------------
    private void setTags(List<String> tags) {
        assertArgNotNull(tags, "태그는 필수입니다.");
        List<String> refactored = TagRefactor.refactor(tags);
        TagRule.rejectIfEmptyOrTooMany(refactored);
        TagRule.rejectIfIllegalNames(refactored);
        this.tags = refactored;
    }

    private void checkBlocked() {
        if (blocked())
            throw new ModelConflicted(format("해당 게시글은 블락되었기 때문에 더 이상 진행할 수 없습니다: '%s'", id));
    }

    private void checkDeleted() {
        if (deleted())
            throw new ModelConflicted(format("해당 게시글은 삭제되었기 때문에 더 이상 진행할 수 없습니다: '%s'", id));
    }

    private void checkDeletedForce() {
        if (deletedForce())
            throw new ModelConflicted(format("해당 게시글은 관리자에 의해 강제 삭제되었기 때문에 더 이상 진행할 수 없습니다: '%s'", id));
    }

    private void setId(String id) {
        assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setBoardId(String boardId) {
        assertArgNotNull(boardId, "게시판 id는 필수입니다.");
        this.boardId = boardId;
    }

    private void setTitle(String title) {
        assertArgNotNull(title, "제목은 필수입니다.");
        String trimed = title.trim();
        assertArgLength(trimed, 1, 150, format("게시글 제목은 최대 %d~%d자까지만 가능합니다: 현재 %,d자", 1, 150, trimed.length()));
        this.title = trimed;
    }

    private void setBody(String body) {
        assertArgNotNull(body, "바디는 필수입니다.");
        String trimed = body.trim();
        assertArgLength(trimed, 5, 3000, format("게시글 본문은 %d~%,d자까지만 가능합니다: 현재 %,d자", 5, 3000, trimed.length()));
        this.body = trimed;
    }

    private void setWriterId(String writerId) {
        assertArgNotNull(writerId, "작성자 id는 필수입니다.");
        this.writerId = writerId;
    }

    private void setWriterName(String writerName) {
        assertArgNotNull(writerName, "작성자 이름은 필수입니다.");
        this.writerName = writerName;
    }

    private void setHitCount(long hitCount) {
        assertArgNotNull(hitCount, "조회수는 필수입니다.");
        assertArgNonNegative(hitCount, "조회수는 0 이상어이야 합니다.");
        this.hitCount = hitCount;
    }

    private void setStatus(ArticleStatus status) {
        assertArgNotNull(status, "상태는 필수입니다.");
        this.status = status;
    }
}
