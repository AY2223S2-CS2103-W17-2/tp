package seedu.dengue.logic.commands;

import seedu.dengue.logic.commands.exceptions.CommandException;
import seedu.dengue.model.Model;

/**
 * A command class that handles common operations between {@link UndoCommand} and @{link RedoCommand}.
 */
abstract class UndoRedoCommand extends Command {
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Undo or redo actions by the user for a maximum number of times possible.
     * @param model The model to undo or redo.
     * @param numOfSteps An upper bound on the number of steps to undo or redo.
     * @param isUndo Boolean value that checks to undo or redo.
     * @return The maximum number of undo or redo operations possible that is less than {@param numOfSteps}.
     */
    public int undoOrRedoAtMost(Model model, int numOfSteps, boolean isUndo) {
        int counts;
        if (isUndo) {
            for (counts = 0; counts < numOfSteps; counts++) {
                try {
                    model.undo();
                } catch (CommandException err) {
                    break;
                }
            }
            return counts;
        } else {
            for (counts = 0; counts < numOfSteps; counts++) {
                try {
                    model.redo();
                } catch (CommandException err) {
                    break;
                }
            }
            return counts;
        }
    }
}
