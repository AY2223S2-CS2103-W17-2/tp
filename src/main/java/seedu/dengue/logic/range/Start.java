package seedu.dengue.logic.range;

import seedu.dengue.model.person.Person;

/**
 * Represents the start of a given range
 */
public interface Start<T> {
    boolean isBefore(Person p);
    T get();
}
