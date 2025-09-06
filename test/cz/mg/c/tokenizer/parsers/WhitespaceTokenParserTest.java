package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Test;
import cz.mg.c.tokenizer.test.TokenParserTester;
import cz.mg.token.tokens.WhitespaceToken;

public @Test class WhitespaceTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + WhitespaceTokenParserTest.class.getSimpleName() + " ... ");

        WhitespaceTokenParserTest test = new WhitespaceTokenParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(
            WhitespaceTokenParser.getInstance(), 0, 0, WhitespaceToken::new
        );
        tester.testParse("");
        tester.testParse("1");
        tester.testParse("3.14");
        tester.testParse("+");
        tester.testParse("", " ", "");
        tester.testParse("", "\n", "");
        tester.testParse("", "\t", "");
        tester.testParse("", "  ", "");
        tester.testParse("", "   ", "");
        tester.testParse("", "    ", "");
        tester.testParse("", "\n", "\n");
        tester.testParse("", "\t", "\t");
        tester.testParse("1", " ", "1");
        tester.testParse("a", "\n", "b");
        tester.testParse("(", "\t", ")");
    }
}