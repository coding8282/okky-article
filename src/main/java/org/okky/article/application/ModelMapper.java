package org.okky.article.application;

import lombok.experimental.FieldDefaults;
import org.okky.article.application.command.CreateBoardCommand;
import org.okky.article.application.command.WriteArticleCommand;
import org.okky.article.domain.model.Article;
import org.okky.article.domain.model.Board;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE)
public class ModelMapper {
    Article toModel(WriteArticleCommand cmd) {
        return new Article(
                cmd.getBoardId(),
                cmd.getTitle(),
                cmd.getBody(),
                cmd.getWriterId(),
                cmd.getWriterName(),
                cmd.getTags()
        );
    }

    Board toModel(CreateBoardCommand cmd) {
        return new Board(
                cmd.getName(),
                cmd.getShortDescription(),
                cmd.getDescription()
        );
    }
}
