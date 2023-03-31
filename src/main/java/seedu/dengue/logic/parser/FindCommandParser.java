package seedu.dengue.logic.parser;

import static seedu.dengue.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.dengue.commons.core.Messages.MESSAGE_INVALID_RANGE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_AGE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_ENDAGE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_ENDDATE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_POSTAL;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_STARTAGE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.dengue.logic.parser.CliSyntax.PREFIX_VARIANT;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.dengue.logic.commands.AddCommand;
import seedu.dengue.logic.commands.FindCommand;
import seedu.dengue.logic.parser.exceptions.ParseException;
import seedu.dengue.model.person.Age;
import seedu.dengue.model.person.ContinuousData;
import seedu.dengue.model.person.Date;
import seedu.dengue.model.person.Name;
import seedu.dengue.model.person.SubPostal;
import seedu.dengue.model.predicate.FindPredicate;
import seedu.dengue.model.range.EndAge;
import seedu.dengue.model.range.EndDate;
import seedu.dengue.model.range.Range;
import seedu.dengue.model.range.StartAge;
import seedu.dengue.model.range.StartDate;
import seedu.dengue.model.variant.Variant;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_POSTAL, PREFIX_DATE,
                        PREFIX_AGE, PREFIX_VARIANT, PREFIX_ENDDATE, PREFIX_STARTDATE,
                        PREFIX_STARTAGE, PREFIX_ENDAGE);
        if (!isValidFormat(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddCommand.MESSAGE_USAGE));
        }

        Optional<Name> name = ParserUtil.parseOptionalName(argMultimap.getValue(PREFIX_NAME));
        Optional<SubPostal> subPostal = ParserUtil.parseOptionalSubPostal(
                argMultimap.getValue(PREFIX_POSTAL));
        Optional<Age> age = ParserUtil.parseOptionalAge(argMultimap.getValue(PREFIX_AGE));
        Optional<Date> date = ParserUtil.parseOptionalDate(argMultimap.getValue(PREFIX_DATE));
        Set<Variant> variantList = ParserUtil.parseVariants(argMultimap.getAllValues(PREFIX_VARIANT));
        Range<Date> dateRange = getDateRange(argMultimap);
        Range<Age> ageRange = getAgeRange(argMultimap);

        FindPredicate canFilter = new FindPredicate(name, subPostal, age, date,
                variantList, dateRange, ageRange);

        return new FindCommand(canFilter);
    }

    private static Range<Date> getDateRange(ArgumentMultimap argumentMultimap) throws ParseException {
        StartDate startDate = new StartDate(ParserUtil.parseOptionalDate(argumentMultimap
                .getValue(PREFIX_STARTDATE)));
        EndDate endDate = new EndDate(ParserUtil.parseOptionalDate(argumentMultimap
                .getValue(PREFIX_ENDDATE)));
        if (!startDate.isBefore(endDate)) {
            throw new ParseException(MESSAGE_INVALID_RANGE);
        }
        return ContinuousData.generateRange(startDate, endDate);
    }

    private static Range<Age> getAgeRange(ArgumentMultimap argumentMultimap) throws ParseException {
        StartAge startAge = new StartAge(ParserUtil.parseOptionalAge(argumentMultimap
                .getValue(PREFIX_STARTAGE)));
        EndAge endAge = new EndAge(ParserUtil.parseOptionalAge(argumentMultimap
                .getValue(PREFIX_ENDAGE)));
        if (!startAge.isBefore(endAge)) {
            throw new ParseException(MESSAGE_INVALID_RANGE);
        }
        return ContinuousData.generateRange(startAge, endAge);
    }
    /**
     * Returns true if at least one of the prefixes contains non-empty {@code Optional} value in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isAPrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
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
     * Returns true if at least one of the prefixes for STARTAGE, ENDAGE is used in conjunction with AGE
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean hasMixedAges(ArgumentMultimap argumentMultimap) {
        return argumentMultimap.getValue(PREFIX_AGE).isPresent()
                & (argumentMultimap.getValue(PREFIX_STARTAGE).isPresent()
                | argumentMultimap.getValue(PREFIX_ENDAGE).isPresent());
    }

    private static boolean isValuePresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }

    /**
     * Returns true if at least one of the prefixes for AGE and DATE are not mixed, and that the user input has
     * at least one prefix, and that the user input contains no preamble.
     */
    private static boolean isValidFormat(ArgumentMultimap argumentMultimap) {
        boolean isCommandNonEmpty = isAPrefixPresent(argumentMultimap, PREFIX_NAME, PREFIX_POSTAL,
                PREFIX_DATE, PREFIX_AGE, PREFIX_VARIANT, PREFIX_ENDDATE,
                PREFIX_STARTDATE, PREFIX_STARTAGE, PREFIX_ENDAGE);
        boolean isPreambleEmpty = argumentMultimap.getPreamble().isEmpty();
        boolean areAgePrefixesNotMixed = !hasMixedAges(argumentMultimap);
        boolean areDatePrefixesNotMixed = !hasMixedDates(argumentMultimap);
        return isCommandNonEmpty && isPreambleEmpty && areAgePrefixesNotMixed && areDatePrefixesNotMixed;
    }
}
