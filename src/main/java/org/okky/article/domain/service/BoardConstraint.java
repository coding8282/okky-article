package org.okky.article.domain.service;

import lombok.AllArgsConstructor;
import org.okky.article.domain.model.Board;
import org.okky.article.domain.repository.ArticleRepository;
import org.okky.article.domain.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.okky.share.execption.ModelConflicted;
import org.okky.share.execption.ModelNotExists;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class BoardConstraint {
    private BoardRepository boardRepository;
    private ArticleRepository articleRepository;

    public void checkExists(String boardId) {
        checkExistsAndGet(boardId);
    }

    public Board checkExistsAndGet(String boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null)
            throw new ModelNotExists(format("해당 게시판은 존재하지 않습니다: '%s'", boardId));
        return board;
    }

    public void checkUniqueName(String boardName) {
        boolean exists = boardRepository.existsByName(boardName);
        if (exists)
            throw new ModelConflicted(format("같은 명칭의 게시판이 이미 존재합니다: '%s'", boardName));
    }

    public void rejectRemoveIfHasArticles(String boardId) {
        long articleCount = articleRepository.countByBoardId(boardId);
        if (articleCount > 0)
            throw new ModelConflicted(format("게시판에 게시글이 존재하기 때문에 삭제할 수 없습니다: '%s'", boardId));
    }
}
