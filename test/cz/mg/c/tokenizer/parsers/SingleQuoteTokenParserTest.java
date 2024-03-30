package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Test;
import cz.mg.token.tokens.quote.SingleQuoteToken;

public @Test class SingleQuoteTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SingleQuoteTokenParserTest.class.getSimpleName() + " ... ");

        SingleQuoteTokenParserTest test = new SingleQuoteTokenParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(
            SingleQuoteTokenParser.getInstance(), 1, 1, SingleQuoteToken.class
        );
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
        tester.testParse("", "'\\\\'", "");
    }
}