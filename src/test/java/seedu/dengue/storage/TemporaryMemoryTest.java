package seedu.dengue.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.dengue.logic.commands.exceptions.CommandException;
import seedu.dengue.model.DengueHotspotTracker;
import seedu.dengue.storage.temporary.TemporaryMemory;
import seedu.dengue.testutil.DengueHotspotTrackerBuilder;

public class TemporaryMemoryTest {
    private DengueHotspotTrackerBuilder builder;
    private DengueHotspotTracker tracker;
    private TemporaryMemory memory;
    private ArrayList<DengueHotspotTracker> inputs;

    // Randomly generated states of DengueHotspotTrackers to test.
    @BeforeEach
    public void setUp() {
        this.builder = new DengueHotspotTrackerBuilder();
        this.tracker = this.builder.buildNew();
        this.memory = new TemporaryMemory(this.tracker);

        this.inputs = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            inputs.add(builder.buildRandom());
        }

    }

    // Positive random test cases.

    @Test
    public void pushManySaves_randomDengueHotspotTrackers_memoryKeepsOnlyTenSaves() {
        int i;
        for (i = 0; i < 9; i++) {
            this.memory.saveNewLatest(this.inputs.get(i));
        }
        assertFalse(this.memory.isFull());
        for (i = i; i < inputs.size(); i++) {
            this.memory.saveNewLatest(this.inputs.get(i++));
            assertEquals(this.memory.getRedoHistory().size(), 10);
        }
    }
    // Saves multiple files into the stack, then perform undo a number of times
    // less than the number of saves.
    @Test
    public void saveMultipleThenUndoFewerTimes_randomNumberOfRandomFiles_loadsCorrectFile()
            throws CommandException {
        Random random = new Random();
        int numFiles = random.nextInt(10) + 3;
        for (int i = 0; i < numFiles; i++) {
            this.memory.saveNewLatest(inputs.get(i));
        }
        int numUndos = random.nextInt(numFiles - 2) + 1;
        for (int j = 0; j < numUndos; j++) {
            this.memory.undo();
        }
        assertEquals(this.memory.loadCurrent(), inputs.get(numFiles - numUndos - 1));
    }

    @Test
    public void undoAndPushNew_randomFiles_temporaryStorageEmpty()
            throws CommandException {
        this.memory.saveNewLatest(inputs.get(0));
        this.memory.saveNewLatest(inputs.get(1));
        this.memory.undo();
        this.memory.saveNewLatest(inputs.get(2));
        assertEquals(this.memory.getUndoHistory().size(), 0);
    }

    // Negative test cases.
    @Test
    public void undoTooManySteps_threeRandomFiles_throwsCommandException()
            throws CommandException {
        this.memory.saveNewLatest(inputs.get(0));
        this.memory.saveNewLatest(inputs.get(1));
        this.memory.undo();
        this.memory.undo();
        assertEquals(this.memory.getRedoHistory().size(), 1);
        assertThrows(CommandException.class, () -> this.memory.undo());
    }

    @Test
    public void redoTooManySteps_threeRandomFiles_throwsCommandException()
            throws CommandException {
        this.memory.saveNewLatest(inputs.get(0));
        this.memory.saveNewLatest(inputs.get(1));
        this.memory.undo();
        this.memory.redo();
        assertThrows(CommandException.class, () -> this.memory.redo());
    }

    @Test
    public void saveAfterUndoThenRedo_randomFiles_throwsCommandException()
            throws CommandException {
        this.memory.saveNewLatest(inputs.get(5));
        this.memory.saveNewLatest(inputs.get(6));
        this.memory.undo();
        this.memory.saveNewLatest(inputs.get(2));
        assertThrows(CommandException.class, () -> this.memory.redo());
    }

}
