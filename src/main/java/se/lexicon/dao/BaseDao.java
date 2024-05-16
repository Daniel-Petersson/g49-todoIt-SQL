package se.lexicon.dao;

import java.util.List;

public interface BaseDao<T> {
    T create(T model);
    T findById(Integer id);
    boolean deleteById(Integer id);
    void update(T model);
    List<T> findAll();
}
