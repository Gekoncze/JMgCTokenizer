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
        test.testParseTooShort();
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
        CharacterReader reader = new CharacterReader("12345671234567");
        Assert.assertEquals(83, parser.parse(reader));
        Assert.assertEquals('4', reader.read());
    }

    private void testParseTooShort() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("1")))
            .withMessage("Octal number must have at least 3 characters.")
            .throwsException(TokenizeException.class);

        Assert.assertThatCode(() -> parser.parse(new CharacterReader("12")))
            .withMessage("Octal number must have at least 3 characters.")
            .throwsException(TokenizeException.class);
    }

    private void testParse() {
        Assert.assertEquals(0, parser.parse(new CharacterReader("000")));
        Assert.assertEquals(1, parser.parse(new CharacterReader("001")));
        Assert.assertEquals(7, parser.parse(new CharacterReader("007")));
        Assert.assertEquals(8, parser.parse(new CharacterReader("010")));
        Assert.assertEquals(9, parser.parse(new CharacterReader("011")));
        Assert.assertEquals(10, parser.parse(new CharacterReader("012")));
        Assert.assertEquals(83, parser.parse(new CharacterReader("123")));
    }

    private void testParseMore() {
        CharacterReader reader = new CharacterReader("1234");
        Assert.assertEquals(83, parser.parse(reader));
        Assert.assertEquals('4', reader.read());
    }
}
