package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Test class EscapeSequenceParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + EscapeSequenceParserTest.class.getSimpleName() + " ... ");

        EscapeSequenceParserTest test = new EscapeSequenceParserTest();
        test.testParseEmpty();
        test.testParseMissing();
        test.testParseIllegal();
        test.testParseSimple();

        System.out.println("OK");
    }

    private final @Service EscapeSequenceParser parser = EscapeSequenceParser.getInstance();

    private void testParseEmpty() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("")))
            .withMessage("Parsing empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseMissing() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("\\")))
            .withMessage("Parsing with missing sequence in input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseIllegal() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("aa")))
            .withMessage("Parsing with illegal input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseSimple() {
        Assert.assertEquals('\n', parser.parse(new CharacterReader("\\n")));
        Assert.assertEquals('\t', parser.parse(new CharacterReader("\\t")));
        Assert.assertEquals('\'', parser.parse(new CharacterReader("\\'")));
        Assert.assertEquals('\"', parser.parse(new CharacterReader("\\" + '"')));
    }
}
