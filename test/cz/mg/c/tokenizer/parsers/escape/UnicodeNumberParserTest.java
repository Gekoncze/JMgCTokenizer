package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;
import cz.mg.test.Assertions;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Test class UnicodeNumberParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + UnicodeNumberParserTest.class.getSimpleName() + " ... ");

        UnicodeNumberParserTest test = new UnicodeNumberParserTest();
        test.testParseEmpty();
        test.testParseIllegal();
        test.testParseNegative();
        test.testParseTooLong();
        test.testParseTooShort();
        test.testParse();
        test.testParseMore();

        System.out.println("OK");
    }

    private final @Service UnicodeNumberParser parser = UnicodeNumberParser.getInstance();

    private void testParseEmpty() {
        Assertions.assertThatCode(() -> parser.parseShort(new CharacterReader("")))
            .withMessage("Empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseLong(new CharacterReader("")))
            .withMessage("Empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseIllegal() {
        Assertions.assertThatCode(() -> parser.parseShort(new CharacterReader("*")))
            .withMessage("Illegal input should throw tokenize exception.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseLong(new CharacterReader("*")))
            .withMessage("Illegal input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseNegative() {
        Assertions.assertThatCode(() -> parser.parseShort(new CharacterReader("-1")))
            .withMessage("Negative numbers are not supported and should throw tokenize exception.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseLong(new CharacterReader("-1")))
            .withMessage("Negative numbers are not supported and should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseTooLong() {
        CharacterReader shortReader = new CharacterReader("123456789ABCDEF");
        Assert.assertEquals(4660, parser.parseShort(shortReader));
        Assert.assertEquals('5', shortReader.read());

        CharacterReader longReader = new CharacterReader("123456789ABCDEF");
        Assert.assertEquals(305419896, parser.parseLong(longReader));
        Assert.assertEquals('9', longReader.read());
    }

    private void testParseTooShort() {
        Assertions.assertThatCode(() -> parser.parseShort(new CharacterReader("1")))
            .withMessage("Unicode number must have 4 characters.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseShort(new CharacterReader("123***")))
            .withMessage("Unicode number must have 4 characters.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseLong(new CharacterReader("1")))
            .withMessage("Unicode number must have 8 characters.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parseLong(new CharacterReader("1234567***")))
            .withMessage("Unicode number must have 8 characters.")
            .throwsException(TokenizeException.class);
    }

    private void testParse() {
        Assert.assertEquals(0, parser.parseShort(new CharacterReader("0000")));
        Assert.assertEquals(105, parser.parseShort(new CharacterReader("0069")));
        Assert.assertEquals(4660, parser.parseShort(new CharacterReader("1234")));

        Assert.assertEquals(0, parser.parseLong(new CharacterReader("00000000")));
        Assert.assertEquals(105, parser.parseLong(new CharacterReader("00000069")));
        Assert.assertEquals(4660, parser.parseLong(new CharacterReader("00001234")));
        Assert.assertEquals(286335523, parser.parseLong(new CharacterReader("11112223")));
    }

    private void testParseMore() {
        CharacterReader shortReader = new CharacterReader("1234foo");
        Assert.assertEquals(4660, parser.parseShort(shortReader));
        Assert.assertEquals('f', shortReader.read());

        CharacterReader longReader = new CharacterReader("00001234foo");
        Assert.assertEquals(4660, parser.parseLong(longReader));
        Assert.assertEquals('f', longReader.read());
    }
}
