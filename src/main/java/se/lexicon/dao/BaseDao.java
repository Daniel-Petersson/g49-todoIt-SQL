package se.lexicon.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    Optional<T> create(T model);
    Optional<T> findById(Integer id);
    Collection<T> find(String input);
    List<T> findAll();
    boolean deleteById(Integer id);
    T update(T model);
}
