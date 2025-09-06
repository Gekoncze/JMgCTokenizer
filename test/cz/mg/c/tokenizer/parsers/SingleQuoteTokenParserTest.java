package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.tokenizer.test.TokenParserTester;
import cz.mg.test.Assert;
import cz.mg.token.tokens.quotes.SingleQuoteToken;
import cz.mg.tokenizer.components.CharacterReader;

public @Test class SingleQuoteTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SingleQuoteTokenParserTest.class.getSimpleName() + " ... ");

        SingleQuoteTokenParserTest test = new SingleQuoteTokenParserTest();
        test.testParse();
        test.testParseEscapeSequence();

        System.out.println("OK");
    }

    private final @Service SingleQuoteTokenParser parser = SingleQuoteTokenParser.getInstance();

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(parser, 1, 1, SingleQuoteToken::new);
        tester.testParse("");
        tester.testException("'");
        tester.testException("test '");
        tester.testParse("int a = 0;");
        tester.testParse("int a = 0; ", "' test'", "");
        tester.testParse("int a = 0; ", "'test'", "");
        tester.testParse("int a = 0; ", "'test '", "");
        tester.testParse("", "'test'", "");
        tester.testParse("", "' test'", "");
        tester.testParse("", "'test '", "");
        tester.testParse("(", "'a'", "");
        tester.testParse("", "'test'", "\nint a = 0;");
        tester.testParse("void", "' test'", "\nint a = 0;");
        tester.testParse("", "'test ('", "");
        tester.testParse("", "'test 1'", "");
        tester.testParse("", "'test a'", "");
        tester.testParse("", "'test //'", "");
        tester.testParse("", "'test /*'", "");
        tester.testParse("", "'test \"'", "'");
    }

    private void testParseEscapeSequence() {
        Assert.assertEquals("n", parse("'n'"));
        Assert.assertEquals("\n", parse("'\\n'"));
        Assert.assertEquals("\"", parse("'\\\"'"));
        Assert.assertEquals("'", parse("'\\''"));
        Assert.assertEquals("test \" test", parse("'test \\\" test'"));
        Assert.assertEquals("test ' test", parse("'test \\' test'"));
        Assert.assertEquals("K", parse("'\\u004B'"));
    }

    private @Mandatory String parse(@Mandatory String input) {
        SingleQuoteToken output = parser.parse(new CharacterReader(input));
        Assert.assertNotNull(output);
        return output.getText();
    }
}