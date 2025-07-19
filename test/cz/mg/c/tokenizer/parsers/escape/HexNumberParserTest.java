package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Test class HexNumberParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + HexNumberParserTest.class.getSimpleName() + " ... ");

        HexNumberParserTest test = new HexNumberParserTest();
        test.testParseEmpty();
        test.testParseIllegal();
        test.testParseNegative();
        test.testParseTooLong();
        test.testParse();
        test.testParseMore();

        System.out.println("OK");
    }


    private final @Service HexNumberParser parser = HexNumberParser.getInstance();

    private void testParseEmpty() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("")))
            .withMessage("Empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseIllegal() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("lol")))
            .withMessage("Illegal input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseNegative() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("-1")))
            .withMessage("Negative numbers are not supported and should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseTooLong() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("123456789ABCDEF")))
            .withMessage("Too long input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParse() {
        Assert.assertEquals(0, parser.parse(new CharacterReader("0")));
        Assert.assertEquals(1, parser.parse(new CharacterReader("1")));
        Assert.assertEquals(7, parser.parse(new CharacterReader("7")));
        Assert.assertEquals(9, parser.parse(new CharacterReader("9")));
        Assert.assertEquals(10, parser.parse(new CharacterReader("A")));
        Assert.assertEquals(10, parser.parse(new CharacterReader("0A")));
        Assert.assertEquals(11, parser.parse(new CharacterReader("B")));
        Assert.assertEquals(12, parser.parse(new CharacterReader("C")));
        Assert.assertEquals(13, parser.parse(new CharacterReader("D")));
        Assert.assertEquals(14, parser.parse(new CharacterReader("E")));
        Assert.assertEquals(15, parser.parse(new CharacterReader("F")));
        Assert.assertEquals(26, parser.parse(new CharacterReader("1A")));
        Assert.assertEquals(127, parser.parse(new CharacterReader("7F")));
        Assert.assertEquals(247, parser.parse(new CharacterReader("F7")));
        Assert.assertEquals(2810, parser.parse(new CharacterReader("AFA")));
        Assert.assertEquals(1026846, parser.parse(new CharacterReader("fAb1E")));
    }

    private void testParseMore() {
        CharacterReader reader = new CharacterReader("ALSA");
        Assert.assertEquals(10, parser.parse(reader));
        Assert.assertEquals('L', reader.read());
    }
}
