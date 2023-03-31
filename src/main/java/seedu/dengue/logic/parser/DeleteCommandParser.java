package seedu.dengue.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.dengue.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.dengue.commons.core.Messages.MESSAGE_INVALID_RANGE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_ENDDATE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_STARTDATE;

import java.util.List;
import java.util.Optional;

import seedu.dengue.commons.core.index.Index;
import seedu.dengue.logic.commands.DeleteCommand;
import seedu.dengue.logic.parser.exceptions.ParseException;
import seedu.dengue.logic.range.EndDate;
import seedu.dengue.logic.range.Range;
import seedu.dengue.logic.range.StartDate;
import seedu.dengue.model.person.Date;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE,
                        PREFIX_STARTDATE, PREFIX_ENDDATE);

        if (hasIndexAndDate(argMultimap)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (noDateProvided(argMultimap)) {
            return parseIndex(args);
        } else {
            return parseDate(argMultimap);
        }
    }

    private DeleteCommand parseIndex(String args) throws ParseException {
        try {
            List<Index> indexes = ParserUtil.parseMultiIndex(args);
            return new DeleteCommand(indexes);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    private DeleteCommand parseDate(ArgumentMultimap argMultimap) throws ParseException {
        try {
            if (hasMixedDates(argMultimap)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteCommand.MESSAGE_USAGE));
            }
            if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
                String date = argMultimap.getValue(PREFIX_DATE).get();
                return new DeleteCommand(new Date(date));
            } else {
                StartDate startDate = new StartDate(ParserUtil.parseOptionalDate(argMultimap
                        .getValue(PREFIX_STARTDATE)));
                EndDate endDate = new EndDate(ParserUtil.parseOptionalDate(argMultimap
                        .getValue(PREFIX_ENDDATE)));
                if (!startDate.isBefore(endDate)) {
                    throw new ParseException(MESSAGE_INVALID_RANGE);
                }
                Range<Date> range = new Range<>(startDate, endDate);
                return new DeleteCommand(range);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

    private static boolean hasIndexAndDate(ArgumentMultimap argMultimap) {

        boolean indexBeforeDate = !argMultimap.getPreamble().isEmpty() & !noDateProvided(argMultimap);

        Optional<String> date = argMultimap.getValue(PREFIX_DATE);
        Optional<String> startDate = argMultimap.getValue(PREFIX_STARTDATE);
        Optional<String> endDate = argMultimap.getValue(PREFIX_ENDDATE);
        boolean indexAfterDate = indexAfterDate(date) | indexAfterDate(startDate) | indexAfterDate(endDate);

        return indexBeforeDate | indexAfterDate;
    }

    /**
     * Tests whether the user has input an index after the date, which should be rejected.
     * Returns true if there is an index after the date, false otherwise.
     * @param date The argument to be tested.
     * @return A boolean.
     */
    private static boolean indexAfterDate(Optional<String> date) {
        if (date.isEmpty()) {
            return false;
        }
        String test = date.get();
        int lastSpace = test.lastIndexOf(" ");
        if (lastSpace == -1) {
            return false;
        } else {
            String slicedDate = test.substring(0, lastSpace);
            return Date.isValidDate(slicedDate);
        }
    }

    /**
     * Returns true if at least one of the prefixes for STARTDATE, ENDDATE is used in conjunction with DATE
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean hasMixedDates(ArgumentMultimap argumentMultimap) {
        return argumentMultimap.getValue(PREFIX_DATE).isPresent()
                & (argumentMultimap.getValue(PREFIX_STARTDATE).isPresent()
                | argumentMultimap.getValue(PREFIX_ENDDATE).isPresent());
    }

    /**
     * Returns true if none of the prefixes for STARTDATE, ENDDATE or DATE has a non-empty value
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean noDateProvided(ArgumentMultimap argumentMultimap) {
        return argumentMultimap.getValue(PREFIX_DATE).isEmpty()
                & argumentMultimap.getValue(PREFIX_STARTDATE).isEmpty()
                & argumentMultimap.getValue(PREFIX_ENDDATE).isEmpty();
    }

}
