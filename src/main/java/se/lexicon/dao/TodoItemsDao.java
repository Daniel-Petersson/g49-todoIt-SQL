package se.lexicon.dao;

import se.lexicon.model.Person;
import se.lexicon.model.TodoItem;

import java.util.Collection;

public interface TodoItemsDao extends BaseDao<TodoItem> {
    Collection<TodoItem> findAllByAssignee(Person person);
    boolean deleteByAssignee(Person assignee);
}
