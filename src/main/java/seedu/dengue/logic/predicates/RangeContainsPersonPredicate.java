package seedu.dengue.logic.predicates;

import seedu.dengue.logic.range.Range;
import seedu.dengue.model.person.Person;

/**
 * Represents the predicate which tests for whether the person in the persons list is reported on the
 * range based on the user input.
 */
public class RangeContainsPersonPredicate extends PredicateUtil<Person> {

    private final Range range;

    public RangeContainsPersonPredicate(Range range) {
        this.range = range;
    }

    @Override
    public boolean test(Person person) {
        return range.getStart().isBefore(person) && range.getEnd().isAfter(person);
    }
}
