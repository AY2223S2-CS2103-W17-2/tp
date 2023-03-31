package seedu.dengue.logic.parser;

import static seedu.dengue.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.Paths;

import seedu.dengue.logic.commands.CheckoutCommand;
import seedu.dengue.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class CheckoutCommandParser implements Parser<CheckoutCommand> {

    public static final String REGEX_PATTERN = "^[A-za-z0-9.]{1,255}$";

    /**
     * Determines if the specified file name is valid based on a regular expression pattern.
     *
     * @param filename the file name to check for validity.
     * @return {@code true} if the file name is valid; {@code false} otherwise.
     * @throws NullPointerException if {@code filename} is {@code null}.
     */
    public static boolean validFileName(String filename) {
        if (filename == null) {
            return false;
        }
        return filename.matches(REGEX_PATTERN);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CheckoutCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!trimmedArgs.endsWith(".csv") | !validFileName(trimmedArgs)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CheckoutCommand.MESSAGE_USAGE));
        } else {
            return new CheckoutCommand(Paths.get(trimmedArgs));
        }
    }

}