package org.okky.article.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.domain.Aggregate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.util.UUID;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.share.domain.AssertionConcern.assertArgLength;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@FieldDefaults(level = PRIVATE)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board implements Aggregate {
    @Id
    @Column(length = 50)
    String id;

    @Column(nullable = false, length = 20)
    String name;

    @Column(nullable = false, length = 30)
    String shortDescription;

    @Column(nullable = false, length = 200)
    String description;

    @CreatedDate
    @Column(nullable = false)
    long createdOn;

    public Board(String name, String shortDescription, String description) {
        setId("b-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setName(name);
        setShortDescription(shortDescription);
        setDescription(description);
    }

    public static Board sample() {
        String name = "사는얘기";
        String shortDescription = "인기 게시판입니다.";
        String description = "옼키 회원들이 가장 애용하는 게시판";
        return new Board(name, shortDescription, description);
    }

    public static void main(String[] args) {
        System.out.println(sample());
    }

    public void modify(String newName, String newShortDescription, String newDescription) {
        setName(newName);
        setShortDescription(newShortDescription);
        setDescription(newDescription);
    }

    public boolean isDifferentName(String boardName) {
        return !name.equalsIgnoreCase(boardName);
    }

    // ---------------------------------
    private void setId(String id) {
        assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setName(String name) {
        assertArgNotNull(name, "게시판 이름은 필수입니다.");
        String trimed = name.trim();
        assertArgLength(trimed, 1, 20, format("게시판 이름은 %d~%d자까지 가능합니다.", 1, 20));
        this.name = trimed;
    }

    private void setShortDescription(String shortDescription) {
        assertArgNotNull(shortDescription, "게시판 간단설명은 필수입니다.");
        String trimed = shortDescription.trim();
        assertArgLength(trimed, 1, 30, format("게시판 간단설명은 %d~%d자까지 가능합니다.", 1, 30));
        this.shortDescription = trimed;
    }

    private void setDescription(String description) {
        assertArgNotNull(description, "게시판 설명은 필수입니다.");
        String trimed = description.trim();
        assertArgLength(trimed, 1, 200, format("게시판설명은 %d~%d자까지 가능합니다.", 1, 200));
        this.description = trimed;
    }
}
