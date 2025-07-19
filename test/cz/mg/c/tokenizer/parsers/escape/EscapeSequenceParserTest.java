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
        test.testParseSimpleMore();
        test.testParseHex();
        test.testParseHexLong();
        test.testParseHexMore();
        test.testParseOct();
        test.testParseOctShort();
        test.testParseOctMore();

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

    private void testParseSimpleMore() {
        CharacterReader reader = new CharacterReader("\\nYay!");
        Assert.assertEquals('\n', parser.parse(reader));
        Assert.assertEquals('Y', reader.read());
    }

    private void testParseHex() {
        Assert.assertEquals('\n', parser.parse(new CharacterReader("\\x0A")));
        Assert.assertEquals('?', parser.parse(new CharacterReader("\\x3F")));
    }

    private void testParseHexLong() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("\\x1234567")))
            .withMessage("Too large hex number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseHexMore() {
        CharacterReader reader = new CharacterReader("\\x0Alone");
        Assert.assertEquals('\n', parser.parse(reader));
        Assert.assertEquals('l', reader.read());
    }

    private void testParseOct() {
        Assert.assertEquals('\n', parser.parse(new CharacterReader("\\012")));
    }

    private void testParseOctShort() {
        Assert.assertThatCode(() -> parser.parse(new CharacterReader("\\1")))
            .withMessage("Too small octal number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseOctMore() {
        CharacterReader reader = new CharacterReader("\\101foo");
        Assert.assertEquals('A', parser.parse(reader));
        Assert.assertEquals('f', reader.read());
    }
}
