package se.lexicon.dao;

import se.lexicon.model.Appuser;
import se.lexicon.model.Person;

import java.util.List;

public interface AppUserDao extends BaseDao<Person> {
    Appuser findByUsername();
    List<Appuser> findByRole();
}
