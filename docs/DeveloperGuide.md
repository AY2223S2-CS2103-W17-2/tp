---
layout: page
title: Developer Guide
---
* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/Main.java) and [`MainApp`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to various platforms of storage.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `DengueHotspotTrackerParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `DengueHotspotTrackerParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `DengueHotspotTrackerParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the Dengue Hotspot Tracker data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Variant` list in the `DengueHotspotTracker`, which `Person` references. This allows `DengueHotspotTracker` to only require one `Variant` object per unique dengue variant, instead of each `Person` needing their own `Variant` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2223S2-CS2103-W17-2/tp/tree/master/src/main/java/seedu/age/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both Dengue Hotspot Tracker data and user preference data in csv format, and read them back into corresponding objects.
* inherits from both `DengueHotspotTrackerStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
* temporarily saves `DengueHotspotTracker` data while the app is running, for `undo` and `redo` commands.
### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `TemporaryMemory`. It extends `SpecialisedStackForMemory`, which implements `StackWithStorage`.
`TemporaryMemory` only stores the 10 most recent actions performed by the user, when the app is open. This means that when the app is closed and open again, the user will not be able to perform an undo or redo.
`TemporaryMemory` can be viewed as a stack which supports additional operations.

1. `TemporaryMemory` only contains 10 saved iterations of the file. Therefore, older iterations are deleted.
2. `TemporaryMemory` supports the redo command, and therefore, after performing an undo, more recent iterations of the file are still stored in an auxiliary storage component.
3. When the user performs an undo and then edits/saves the file once again, more recent iterations of the file must be overwritten. Therefore, this temporary storage is cleared.

`TemporaryMemory`, therefore, is a specialised memory stack, where each item is an iteration of the Dengue Hotspot Tracker file. To be precise, it is `TemporaryMemory` is a stack which holds as attributes a `Deque` for the primary memory stack and an auxiliary storage `Stack`, which temporarily stores popped items.

* `TemporaryMemory#saveNewLatest(ReadOnlyDengueHotspotTracker latest)` — Saves the current Dengue Hotspot Tracker state in its history, pushing it into the primary memory stack represented by a `Deque`.
* `TemporaryMemory#undo()` — Restores the previous Dengue Hotspot Tracker state from its history. This pops an item from the primary memory stack represented by a `Deque` and pushes it into the auxiliary storage stack implemented with a `Stack`.
* `TemporaryMemory#redo()` — Restores a previously undone Dengue Hotspot Tracker state from its history. This pushes an item from the auxiliary `Stack` back into the primary memory stack.
* `TemporaryMemory#loadCurrent()` — Peeks into the top element of the stack and loads it.
These operations are exposed in the `Model` interface as `Model#saveChanges()`, `Model#undo()`, `Model#redo()` and `Model#updateFromMemoryStack()`.
Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `DengueHotspotTracker` will be initialized with the initial Dengue Hotspot Tracker state, and the `TemporaryMemory` stack implementation contains only the current Dengue Hotspot Tracker state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the Dengue Hotspot Tracker. The `delete` command calls `Model#saveChanges()`. The Dengue Hotspot Tracker is modified, and a copy of the modified Dengue Hotspot Tracker is generated by `DengueHotspotTracker#generateDeepCopy()`. This deep copy is pushed into the `TemporaryMemory` stack under its `Deque`.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David d/2000 31 January...` to add a new person. The `add` command also calls `Model#saveChanges()`, causing another modified Dengue Hotspot Tracker state to be deep-copied and saved into the `TemporaryMemory` stack, under its `Deque`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#saveChanges()`, so the Dengue Hotspot Tracker state will not be saved into the `TemporaryMemory`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command pops an item from the primary `Deque` in `TemporaryMemory`, and pushes it into the auxiliary storage `Stack`. The new top-level item in the primary `Deque` stack will be read in as the current file.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If there is only 1 element in the `Deque`, then there are no previous DengueHotspotTracker states to restore. The `undo` command uses `TemporaryMemory#canUndo` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

Step 5. The user again decides that adding the person was not a mistake, and decides to redo the action by executing the `redo` command. The `redo` command pops an item from the auxiliary `Stack` in `TemporaryMemory` and pushes it back into the primary stack `Deque`, where it is being read as the current file.

Step 6. The user now wishes to perform an undo twice. The user executes the `undo 2` command to undo two steps. As with before, 2 iterations of the tracker data are popped from the `TemporaryMemory` primary `Deque` and pushed into the auxiliary `Stack`.

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `TemporaryMemory#redo()`, which pops from the auxiliary `Stack` once, and pushes the popped item back into the primary `Deque`, restoring the Dengue Hotspot Tracker to a previous state.


Step 7. The user then decides to execute the command `list`. Commands that do not modify the Dengue Hotspot Tracker, such as `list`, will usually not call `Model#saveChanges()`, `Model#undo()` or `Model#redo()`. Thus, the `TemporaryMemory` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)


The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves 10 previous iterations of the entire dengue case list temporarily while the app is running.
    * Pros: Saves memory as all tracker iterations are deleted when the app closes. Deleting older tracker iterations also helps to improve performance.
    * Cons: User may not have access to older data.

* **Alternative 2:** Saves 10 previous iterations of the entire dengue case list in a JSON file.
    * Pros: User can have direct access to older data.
    * Cons: Can be very messy to implement.


### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### \[Proposed\] Prefix find Feature

#### Proposed Implementation

The proposed Prefix find feature mechanism is primarily facilitated by the `DengueHotspotTrackerParser#parseCommand()`,
`FindCommandParser#parse()`, `ArgumentTokenizer#Tokenize()`, `ArgumentMultimap#getValue()`, and `FindCommand#execute()`
 methods.

Given below is an example usage scenario and how the Prefix find mechanism behaves at each step.

Step 1. The user launches the application.

Step 2. The user executes the `find a/ 13 n/ Thomas p/ 612` command to filter the list of cases which contain a dengue
 case associated with the name Thomas, who is of the age 13 and has a postal code beginning with 612.

Step 3. `DengueHotspotTrackerParser#parseCommand()` parses the command and, detecting the `find` command word,
 passes the argument `a/ 13 n/ Thomas p/ 612` to the `FindCommandParser`.

Step 4. `FindCommandParser#parse()` will call on `ArgumentTokenizer#Tokenize()` and subsequently gets the values of
 each individual Prefix using `ArgumentMultimap#getValue()`.

Step 5. `FindCommand#execute()` will get the most updated list of filtered Persons based on the values given from
`ArgumentMultimap#getValue()` and shows it on the User Interface.

The following sequence diagram summarises what happens when a user executes a Prefix find operation:

![PrefixFindSequenceDiagram](images/PrefixFindSequenceDiagram.png)

The following activity diagram summarises what happens when a user executes a Prefix find operation:

![PrefixFindActivityDiagram](images/PrefixFindActivityDiagram.png)

#### Design Considerations

**Aspect: How Prefix find handle input that does not make sense (Numerics in names, non-existent postal codes
or age past 200)**

* **Alternative 1 (current choice):** Accepts the user input and executes the find command as per usual.
  * Pros: Allows for user freedom in cases that there may be people with Numerics in names, and the underlying code
would not have to change if new postal codes were to be implemented.
  * Cons: In the case of the input being erroneous, there is no indication that the for the user that it may be due to
what they keyed in.


* **Alternative 2:** Displays a message indicating that the input may be erroneous for each of the available prefix.
  * Pros: In the case of the input being erroneous, there would be an indication that the for the user that their input
may be unintended.
  * Cons: Less flexibility and requires changes to the code base if new postal codes are added.

### \[Proposed\] Multi-index delete feature

#### Proposed Implementation

The proposed multi-index delete mechanism is primarily facilitated by the `DengueHotspotTrackerParser#parseCommand()`, `DeleteCommandParser#parse()`, and `DeleteCommand#execute()` methods.

Given below is an example usage scenario and how the multi-index delete mechanism behaves at each step.

Step 1. The user launches the application and uses the `find` command to filter the list of cases. The `ModelManager`’s `FilteredList<Person>` is updated.

Step 2. The user executes the `delete 1 3` command to delete the first and third persons in the filtered list currently being shown. `DengueHotspotTrackerParser#parseCommand()` parses the command and, detecting the `delete` command word, passes the argument `1 3` to the `DeleteCommandParser`.

Step 3. `DeleteCommandParser#parse()` is called. A list of valid indexes `List<Index>` is returned, and a `DeleteCommand` is constructed, taking in this list of indexes as an attribute.

Step 4. `DeleteCommand#execute()` will get the most updated list of filtered persons and loop through the list of indexes to delete their associated cases using `Model#deletePerson()`. Users will be notified with a message upon successful deletion of all relevant persons.

The following sequence diagram shows how the multi-index delete operation works:

![DeleteMultiIndexSequenceDiagram](images/DeleteMultiIndexSequenceDiagram.png)

The following activity diagram summarises what happens when a user executes a multi-index delete command:

![DeleteMultiIndexActivityDiagram](images/DeleteMultiIndexActivityDiagram.png)

#### Design Considerations

**Aspect: How multi-index delete indicates successful execution:**

* **Alternative 1 (current choice):** Display a message indicating that a number of cases were successfully deleted, the number of cases corresponding to the size of the list of indexes.
    * Pros: Short and succinct, without taking up too much space on the GUI.
    * Cons: Does not show exactly which cases were deleted.

* **Alternative 2:** Display a message indicating successful deletion for each individual deleted case, along with the details of the deleted case.
    * Pros: Shows exactly which cases were deleted for easy validation.
    * Cons: Unnecessarily lengthy; may take up too much space if many cases were deleted at once.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* NEA personnel in charge of delegating personnel to dengue hotspots in order to mitigate the spread of dengue via pest control
* has some mastery over using a computer
* prefers typing to other means of input

**Value proposition**: Inefficient allocation of pest control resources will cost excess time and money. This app will help optimise resources by tracking dengue cases, classifying them by neighbourhood, and identifying clusters.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​  | I want to …​                                                             | So that I can…​                                                      |
| -------- |----------|--------------------------------------------------------------------------|----------------------------------------------------------------------|
| `* * *`  | new user | easily view a help page                                                  | know how to use the app                                              |
| `* * *`  | user     | add a new dengue case                                                    | keep track of the dengue cases in Singapore                          |
| `* * *`  | user     | delete cases from the dengue case list by case ID                        | remove entries that I no longer need                                 |
| `* * *`  | user     | find dengue cases whose postal codes start with any of the input numbers | locate details of cases without having to go through the entire list |
| `* *`    | user     | export the data from the dengue case list from a database in a csv file  | analyse them outside of the app              |
| `*`      | new user | clear all entries                                                        | start over with an empty data set                                               |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `DengueHotspotTracker` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  DengueHotspotTracker shows a list of persons
3.  User requests to delete a specific person in the list
4.  DengueHotspotTracker deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. DengueHotspotTracker shows an error message.

      Use case resumes at step 2.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 dengue cases without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases … }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
