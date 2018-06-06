package org.okky.article.domain.repository;

import org.okky.article.domain.model.Board;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = Board.class, idClass = String.class)
public interface BoardRepository {
    void save(Board board);
    Optional<Board> findById(String id);
    Iterable<Board> findAll();
    void delete(Board board);
    boolean existsByName(String name);
}
