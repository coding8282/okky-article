package org.okky.article.domain.repository.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

@Getter
public class ArticleDto {
    String id;
    String boardId;
    String boardName;
    String writerId;
    String writerName;
    Long wroteOn;
    Long updatedOn;
    Long choosedOn;
    String title;
    String body;
    String bodyReduced;
    Long hitCount;
    Long scrapCount;
    Boolean wroteByMe;
    Boolean scrappedByMe;
    Boolean blocked;
    Boolean choosed;
    Boolean deleted;
    Boolean deletedForce;
    String status;
    @JsonInclude(ALWAYS)
    List<String> tags;
}
