package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Test;
import cz.mg.c.tokenizer.test.TokenParserTester;
import cz.mg.token.tokens.NumberToken;

public @Test class NumberTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + NumberTokenParserTest.class.getSimpleName() + " ... ");

        NumberTokenParserTest test = new NumberTokenParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(
            NumberTokenParser.getInstance(), 0, 0, NumberToken::new
        );
        tester.testParse("");
        tester.testParse("a");
        tester.testParse("a,b,c");
        tester.testParse("", "1", "");
        tester.testParse("", "3.14", "");
        tester.testParse("", "3.14f", "");
        tester.testParse("", "3.14F", "");
        tester.testParse("", "3", ",14");
        tester.testParse("a", "1", "/");
        tester.testParse("x", "3", ",14");
        tester.testParse("-", "0x2A", "*");
        tester.testParse("(", "012", ")");
        tester.testParse(" ", "3.14", " ");
        tester.testParse("*", "3.14", "+");
        tester.testParse("+", "3.14", "/");
        tester.testParse(",", "0123456789", ",");
    }
}