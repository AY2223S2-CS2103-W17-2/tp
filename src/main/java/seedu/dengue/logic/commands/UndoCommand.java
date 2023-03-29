package seedu.dengue.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.dengue.logic.commands.exceptions.CommandException;
import seedu.dengue.model.Model;

/**
 * A command that reverses a change previously made, or reverses a redo operation.
 */
public class UndoCommand extends UndoRedoCommand {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undid %s operations successfully!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " performs an undo operation, where an optional argument \n"
            + "can be provided to indicate the number of operations. \n"
            + "eg. undo 1";

    private final int numberOfUndos;

    /**
     * Creates a {@code UndoCommand} to undo previous actions by the user.
     * @param numberOfUndos The number of steps to undo.
     */
    public UndoCommand(int numberOfUndos) {
        this.numberOfUndos = numberOfUndos;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.undo();
        int counts = 1 + undoOrRedoAtMost(model, this.numberOfUndos - 1, true);
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, counts));
    }
}