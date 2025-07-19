package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Test class OctalNumberParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + OctalNumberParserTest.class.getSimpleName() + " ... ");

        OctalNumberParserTest test = new OctalNumberParserTest();
        test.testParseEmpty();
        test.testParseIllegal();
        test.testParseNegative();
        test.testParseTooLong();
        test.testParse();
        test.testParseMore();

        System.out.println("OK");
    }


    private final @Service OctalNumberParser parser = OctalNumberParser.getInstance();

    private void testParseEmpty() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("")))
            .withMessage("Empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseIllegal() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("8")))
            .withMessage("Illegal input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseNegative() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("-1")))
            .withMessage("Negative numbers are not supported and should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseTooLong() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("12345671234567")))
            .withMessage("Too long input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParse() {
        Assert.assertEquals(0, parser.parse(new CharacterReader("0")));
        Assert.assertEquals(1, parser.parse(new CharacterReader("1")));
        Assert.assertEquals(7, parser.parse(new CharacterReader("7")));
        Assert.assertEquals(8, parser.parse(new CharacterReader("10")));
        Assert.assertEquals(9, parser.parse(new CharacterReader("11")));
        Assert.assertEquals(10, parser.parse(new CharacterReader("12")));
    }

    private void testParseMore() {
        CharacterReader reader = new CharacterReader("19");
        Assert.assertEquals(1, parser.parse(reader));
        Assert.assertEquals('9', reader.read());
    }
}
