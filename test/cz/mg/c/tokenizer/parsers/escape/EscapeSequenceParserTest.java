package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;
import cz.mg.test.Assertions;
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
        test.testParseOctal();
        test.testParseOctalShort();
        test.testParseOctalMore();
        test.testParseUnicode();
        test.testParseUnicodeShort();
        test.testParseUnicodeMore();
        test.testParseUnicodeInvalid();

        System.out.println("OK");
    }

    private final @Service EscapeSequenceParser parser = EscapeSequenceParser.getInstance();

    private void testParseEmpty() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("")))
            .withMessage("Parsing empty input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseMissing() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\")))
            .withMessage("Parsing with missing sequence in input should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseIllegal() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("aa")))
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
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\x1234567")))
            .withMessage("Too large hex number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseHexMore() {
        CharacterReader reader = new CharacterReader("\\x0Alone");
        Assert.assertEquals('\n', parser.parse(reader));
        Assert.assertEquals('l', reader.read());
    }

    private void testParseOctal() {
        Assert.assertEquals('\n', parser.parse(new CharacterReader("\\012")));
    }

    private void testParseOctalShort() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\1")))
            .withMessage("Too small octal number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseOctalMore() {
        CharacterReader reader = new CharacterReader("\\101foo");
        Assert.assertEquals('A', parser.parse(reader));
        Assert.assertEquals('f', reader.read());
    }

    private void testParseUnicode() {
        Assert.assertEquals('O', parser.parse(new CharacterReader("\\u004F")));
        Assert.assertEquals('P', parser.parse(new CharacterReader("\\U00000050")));
    }

    private void testParseUnicodeShort() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\u04F")))
            .withMessage("Too small unicode number should throw tokenize exception.")
            .throwsException(TokenizeException.class);

        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\U0000050")))
            .withMessage("Too small unicode number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }

    private void testParseUnicodeMore() {
        CharacterReader readerShort = new CharacterReader("\\u004Fake");
        Assert.assertEquals('O', parser.parse(readerShort));
        Assert.assertEquals('a', readerShort.read());

        CharacterReader readerLong = new CharacterReader("\\U00000050*");
        Assert.assertEquals('P', parser.parse(readerLong));
        Assert.assertEquals('*', readerLong.read());
    }

    private void testParseUnicodeInvalid() {
        Assertions.assertThatCode(() -> parser.parse(new CharacterReader("\\U0011FFFF")))
            .withMessage("Invalid unicode number should throw tokenize exception.")
            .throwsException(TokenizeException.class);
    }
}
