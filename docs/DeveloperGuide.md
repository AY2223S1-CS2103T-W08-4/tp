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

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


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

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `TaskListPanel`, `InventoryPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Task` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
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
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the task list data i.e., all `Task` objects (which are contained in a `ObservableList` object) that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the inventory data i.e., all `SupplyItem` objects (which are contained in a `ObservableList` object) that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other four components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save address book data, task list data, inventory data, and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage`, `TaskListStorage`, `InventoryStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
* `JsonAdaptedTaskTag`, `JsonAdaptedSupplyItemTag` and `JsonAdaptedPersonTag` are actually the non-ambiguous direct representation of `JsonAdaptedTag`.

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Developed\] Mark/un-mark feature

**Implementation**

**NOTE**: `markTask` and `unmarkTask` has it's implementation done in a similar fashion and hence the below guide will be for
`markTask` only.

This feature allows tasks to be marked as completed or not completed, using the `mark` or `unmark` command. This is facilitated
via the `MarkTaskCommand` and `MarkTaskCommandParser` classes.

The `MarkTaskCommandParser` parses the input given by the user and ensures the validity of the index. Tasks at the target index
will then be marked as completed with the help of the following methods:

* `Model#getFilteredTaskList()` Retrieves the `Task` indicated by its index from `ObservableList<Task>`.
* `Model#setTask(markedTask, targetIndex)` which sets the `status` class variable of the `markedTask` to `true`.
* `MarkTaskCommand#createMarkedTask(taskToMark, configureMarkedTask)` will create a marked task based on the configuration of `configureMarkedTask`.

Given below is an example of how `MarkTaskCommand` is being executed.

**Steps**

Step 1. The user enters the `markTask 1` command.

Step 2. The `MarkTaskCommandParser` parses the index and ensures that the index given is valid. In this process, a `MarkTaskCommand` object with the argument containing the target index is created.

Step 3. The `MarkTaskCommand` is executed. Task of index 1 is retrieved by calling the `ObservableList#get()` method.

![MarkTaskState0](images/MarkTaskState0.png)

Step 4. If the task exists at index 1, then the marked `Task` will be created by calling
`MarkTaskCommand#createMarkedTask(taskToMark, configureMarkedTask)`. Then, `Model#setTask(taskToMark, targetIndex)` will be called which sets the `Task` at index 1 to be a marked `Task`.

![MarkTaskState1](images/MarkTaskState1.png)

Step 5. `CommandResult` is then returned, notifying the user that the `Task` has been marked as completed successfully.

**Activity Diagram**

The user flow is illustrated in the *Activity Diagram* as shown below.

![MarkTaskActivityDiagram](images/MarkTaskActivityDiagram.png)

**Design Considerations**

**Aspect: Utilising toggling instead of setting `true`:**

* **Choice 1 (Chosen Method): Sets status of a `Task` as `true`**
  * Advantages: Gives the developer more control to handle the `Task`, more towards extensibility for future iterations where we might need to have extra parameters to be updated when a `Task` is completed.
  * Disadvantages: More tedious to maintain due to more code being involved.
* **Choice 2: Toggles the current status of `Task` to `true` if the `Task` is not completed otherwise does nothing to the `Task`**
  * Advantages: Easier to maintain for the developer. Acts like a switch and when a `Task` is already completed, marking it again will do nothing (Saves more computational power).
  * Disadvantages: Harder for future extension in the event that we need to do much more than just toggling the status of a `Task`.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

* has a need to manage a significant number of suppliers
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage suppliers faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                        | I want to …​                                                                    | So that I can…​                                                         |
|----------|------------------------------------------------|---------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| `* * *`  | new user                                       | see usage instructions                                                          | refer to instructions when I forget how to use the Salesy App           |
| `* * *`  | vendor                                         | add a new supplier                                                              |                                                                         |
| `* * *`  | vendor                                         | add a new task by specifying task description only                              | keep track of a to do list of tasks                                     |
| `* * *`  | vendor                                         | delete a task                                                                   | remove tasks that I no longer need                                      |
| `* * *`  | vendor                                         | delete a supplier                                                               | remove suppliers that I no longer interact with                         |
| `* * *`  | vendor                                         | find a supplier by company name                                                 | locate details of supplier without having to go through the entire list |
| `* * *`  | vendor                                         | view the tasks that has the earliest deadline                                   | do tasks that matter more first                                         |
| `* * *`  | vendor                                         | mark my task as complete / imcomplete                                           | keep track of my to do list                                             |
| `* * *`  | vendor                                         | increment supplies                                                              | update supplies                                                         |
| `* * *`  | vendor                                         | view my command status and result in a GUI view                                 | have a sense of whether my command is a success or failure              |
| `* * *`  | vendor                                         | deduct supplies from supplier                                                   | keep track of what supplies are remaining                               |
| `* *`    | vendor with many suppliers in Salesy           | sort suppliers by type of product they supply to my stall                       | locate a supplier easily                                                |
| `* *`    | vendor with many hours of usage in Salesy      | add a new supplier with all related information in one go                       | avoid making multiple commands                                          |
| `* *`    | vendor with many hours of usage in Salesy      | add a new task with all related information in one go                           | avoid making multiple commands                                          |
| `* *`    | vendor                                         | update the task description                                                     | make last minute changes to the task if needed                          |
| `* *`    | vendor                                         | update supplier details                                                         | make changes to supplier if needed                                      |
| `* *`    | vendor with many suppliers and tasks in Salesy | have a summary page of relevant information of suppliers and tasks in one place | have a good overview without using the CLI at all                       |
| `* *`    | first time and experienced vendor user         | be given command tips when I enter the wrong command                            | refer to insturction when I forget how to use the Salesy App            |
| `* *`    | vendor                                         | find supplies that are running low                                              | replenish low stock supplies in advance                                 |
| `*`      | vendor with many suppliers in Salesy           | view suppliers in a sorted manner                                               | locate a supplier easily                                                |
| `*`      | vendor with many tasks in Salesy               | sort tasks by name                                                              | locate a task easily                                                    |
| `*`      | vendor                                         | keep track of sales revenue, expenses and profit                                | see if my business is doing well                                        |
| `*`      | vendor that is a long time user                | sort inventory into different categories                                        | keep track of different types of inventory(hardware, perishables, fuel) |
| `*`      | vendor                                         | view ingredients needed for recepies                                            | know what to get from suppliers                                         |
| `*`      | vendor                                         | have a view of a queue system of my orders                                      | know when each supplier will come and replenish the order               |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is `Salesy` and the **Actor** is the `canteen vendor`, unless specified otherwise)

**Use case: View the help page**

**MSS**

1. Vendor clicks on the help tab
2. Salesy shows the various commands that are available to use.
3. Vendor clicks on the <u>drop down of the task </u> that she wants to further expand
4. <u>Drop down</u> will expand and vendor is able to view the command guide.

   Use case ends

**Use case: Add a task**

**MSS**

1. Vendor enters the command to add task
2. Salesy shows that command is successful and displays the task added

   Use case ends.

**Extensions**

* 2a. Salesy detects that the command entered is in an invalid format.
    * 2a1. Salesy shows an error message as well as a guide on the add task command.

      Use case resumes at step 1.

**Use case: Mark a task as done**

**MSS**

1. Vendor requests to <u>list pending tasks</u>.
2. Salesy shows a list of tasks
3. Vendor requests to mark a specific task in the list as done.
4. Salesy marks the task as done.

   Use case ends.

**Extensions**

* 2a. The list is empty

  Use case ends.
* 2b. The given index is invalid.

    * 2b1. Salesy shows an error message.

  Use case resumes at step 2.

**Use case: Decrement supplies**

**MSS**

1. Vendor requests to list the inventory.
2. Salesy shows a list of items.
3. Vendor requests to decrement an item in the list by a specified quantity.
4. Salesy decrements the inventory.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 4a. The amount to be decremented results in negative quantity

    * 4a1. Salesy displays an error message.

      Use case resumes at step 3.

**Use case: Delete a task**

**MSS**

1.  Vendor requests to list tasks
2.  Salesy shows a list of tasks
3.  Vendor requests to delete a specific task in the list
4.  Salesy deletes the task

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. Salesy shows an error message.

      Use case resumes at step 2.

**Use case: View pending tasks**

**MSS**

1. Vendor requests to view pending tasks
2. Salesy shows a list of tasks that are marked undone.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

**Use case: Add a supplier**

**MSS**

1. Vendor requests to add supplier.
2. Salesy adds the supplier and displays the details of the added supplier

   Use case ends.

**Extensions**

* 2a. The command entered is in an invalid format.
    * 2a1. Salesy shows an error message as well as a guide on the add supplier command.
* 2b. A supplier with the same name/phone already exists.
    * 2b1. Salesy shows an error message that the supplier already exists.

    Use case resumes at step 1.

**Use case: Delete a supplier**

**MSS**

1. Vendor requests to delete supplier
2. Salesy deletes the supplier and displays the details of the deleted supplier

   Use case ends.

**Extensions**

* 2a. The command entered is in an invalid format.
    * 2a1. Salesy shows an error message as well as a guide on the delete supplier command.
* 2b. The index number specified is invalid.
    * 2b1. Salesy shows an error message that the index provided is invalid.

    Use case resumes at step 1.

**Use case: List all existing suppliers**

**MSS**

1. Vendor requests list suppliers
2. Salesy displays all existing suppliers

   Use case ends.

**Use case: Find a supplier**

**MSS**

1. Vendor requests to find a supplier.
2. Salesy shows a list of matching suppliers based on vendor's query.

   Use case ends.

**Extensions**

* 2a. The list is empty.
  * 2a1. Salesy displays an empty list.
* 2b. No existing suppliers match the vendor's query.
  * 2b1. Salesy displays an empty list.
  
  Use case ends.

**Use case: Edit a supplier**

**MSS**

1. Vendor requests to edit a supplier
2. Salesy edits the supplier and displays the updated details of the edited supplier

   Use case ends.

**Extensions**

* 2a. The command entered is in an invalid format.
    * 2a1. Salesy shows an error message as well as a guide on the edit supplier command.
* 2b. The index number to specify a supplier to edit is invalid.
    * 2b1. Salesy shows an error message that the index provided is invalid.
* 2c. The new name/phone for the edited supplier conflicts with an existing supplier's name/phone.
    * 2c1. Salesy shows an error message that there is already an existing supplier with the provided details.

  Use case resumes at step 1.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be able to finish executing valid commands within 1 second.
5. Should be able to work without an internet connection.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Query**: actual word or string of words that user wants to search
* **GUI**: Graphical User Interface
* **CLI**: Command Line Interface

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

1. _{ more test cases …​ }_

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

2. _{ more test cases …​ }_       
