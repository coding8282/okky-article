package org.okky.article.application;

import org.okky.article.application.command.CreateBoardCommand;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.model.Article;
import org.okky.article.domain.model.Board;

public interface ModelMapper {
    static Board toBoard(CreateBoardCommand cmd) {
        return new Board(
                cmd.getName(),
                cmd.getShortDescription(),
                cmd.getDescription()
        );
    }

    static Article toArticle(WriteArticleCommand cmd) {
        return new Article(
                cmd.getBoardId(),
                cmd.getTitle(),
                cmd.getBody(),
                cmd.getWriterId(),
                cmd.getWriterName(),
                cmd.getTags()
        );
    }
}
