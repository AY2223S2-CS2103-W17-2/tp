package seedu.dengue.logic.analyst;

import static java.util.Objects.requireNonNull;

import seedu.dengue.model.person.Person;

/**
 * Simulates the bins of a histogram.
 * This is meant to streamline the process of bulk data analysis.
 *
 * @see Analyst
 */
public class DataBin implements Comparable<DataBin> {
    private final String binName;
    private int binSize;

    /**
     * Constructs a {@code DataBin} with the given name.
     *
     * @param binName Name of the data bin.
     */
    public DataBin(String binName) {
        this.binName = binName;
        this.binSize = 0;
    }

    /**
     * Adds the {@code person} to the data bin.
     */
    public void addPerson(Person person) {
        requireNonNull(person);
        this.binSize += 1;
    }

    /**
     * Returns the name of the data bin.
     *
     * @return The name of the data bin.
     */
    public String getName() {
        return this.binName;
    }

    /**
     * Returns the size of the data bin.
     *
     * @return The size of the data bin.
     */
    public int getSize() {
        return this.binSize;
    }

    /**
     * Returns {@code true} if there is no {@code Person} counted in the data bin.
     *
     * @return {@code true} if the data bin has is empty.
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DataBin)) {
            return false;
        }

        return compareTo((DataBin) other) == 0;
    }

    @Override
    public int compareTo(DataBin other) {
        return getSize() - other.getSize();
    }

    @Override
    public String toString() {
        return String.format("Bin '%s': %s",
                getName(), getSize());
    }
}