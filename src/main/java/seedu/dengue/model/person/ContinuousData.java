package seedu.dengue.model.person;

import seedu.dengue.logic.range.End;
import seedu.dengue.logic.range.Range;
import seedu.dengue.logic.range.Start;

/**
 * An interface for {@link Age} and {@link Date} classes, both of which are continuous.
 */
public interface ContinuousData {
    boolean equals(Object other);

    /**
     * Generates a range of continuous data.
     * @param start The start of the range.
     * @param end The end of the range.
     * @return A {@code Range<ContinuousData>}.
     */
    static <R extends ContinuousData> Range<R> generateRange(Start<R> start, End<R> end) {
        return Range.<R>of(start, end);
    }
}
