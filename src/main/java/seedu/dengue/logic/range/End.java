package seedu.dengue.logic.range;

import seedu.dengue.model.person.Person;

/**
 * Represents the end of a given range
 */
public interface End<T> {
    boolean isAfter(Person p);
    T get();
}
