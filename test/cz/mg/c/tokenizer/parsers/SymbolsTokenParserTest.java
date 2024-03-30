package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Test;
import cz.mg.token.tokens.SymbolToken;

public @Test class SymbolsTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SymbolsTokenParserTest.class.getSimpleName() + " ... ");

        SymbolsTokenParserTest test = new SymbolsTokenParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(
            SymbolsTokenParser.getInstance(), 0, 0, SymbolToken.class
        );
        tester.testParse("");
        tester.testParse("1");
        tester.testParse("foo");
        tester.testParse("_");
        tester.testParse("@");
        tester.testParse(",");
        tester.testParse("(");
        tester.testParse("", "+", "");
        tester.testParse("", "=", "");
        tester.testParse("", "~", "");
        tester.testParse("", "!", "");
        tester.testParse("", "?:", "");
        tester.testParse("", "++", "");
        tester.testParse("", ">=", "");
        tester.testParse("3", "-", "1");
        tester.testParse("a", "<", "2");
        tester.testParse("B", "?", "3");
        tester.testParse("1", "==", "A");
        tester.testParse("2", "*", "b");
        tester.testParse(" ", "/=", " ");
    }
}