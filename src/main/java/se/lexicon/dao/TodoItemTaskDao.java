package se.lexicon.dao;

import se.lexicon.model.TodoItem;
import se.lexicon.model.TodoItemTask;

import java.util.Collection;
import java.util.function.Predicate;

public interface TodoItemTaskDao extends BaseDao<TodoItemTask> {
    Collection<TodoItem> find(Predicate<TodoItem> filter);
}
