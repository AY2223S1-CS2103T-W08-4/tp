package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    public static final String COMMAND_WORD = "find";
    public static final String VAlID_KEYWORD_NO_SPACE = " n/Alice Bob";
    public static final String VAlID_KEYWORD_WITH_SPACE = " n/ \n Alice \n \t Bob  \t";

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, COMMAND_WORD + VALID_KEYWORD_NO_SPACE, expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, COMMAND_WORD + VALID_KEYWORD_WITH_SPACE, expectedFindCommand);
    }

}
