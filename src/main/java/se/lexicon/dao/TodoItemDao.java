package se.lexicon.dao;

import se.lexicon.model.Person;
import se.lexicon.model.TodoItem;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface TodoItemDao extends BaseDao<TodoItem>{
    List<TodoItem> findAll();
    Collection<TodoItem> find(Predicate<TodoItem> filter);
}
