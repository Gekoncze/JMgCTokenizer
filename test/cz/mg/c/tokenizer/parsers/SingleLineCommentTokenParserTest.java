package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Test;
import cz.mg.token.tokens.comment.SingleLineCommentToken;

public @Test class SingleLineCommentTokenParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SingleLineCommentTokenParserTest.class.getSimpleName() + " ... ");

        SingleLineCommentTokenParserTest test = new SingleLineCommentTokenParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private void testParse() {
        TokenParserTester tester = new TokenParserTester(
            SingleLineCommentTokenParser.getInstance(), 2, 0, SingleLineCommentToken.class
        );
        tester.testParse("");
        tester.testParse("int a = 0;");
        tester.testParse("int a = 0; ", "// test", "");
        tester.testParse("int a = 0; ", "//test", "");
        tester.testParse("int a = 0; ", "//test ", "");
        tester.testParse("", "//test", "");
        tester.testParse("", "// test", "");
        tester.testParse("", "//test ", "");
        tester.testParse("/* test */");
        tester.testParse("(", "//a", "");
        tester.testParse("", "// test", "\nint a = 0;");
        tester.testParse("void", "// test", "\nint a = 0;");
        tester.testParse("", "// test (", "");
        tester.testParse("", "// test a", "");
        tester.testParse("", "// test 1", "");
        tester.testParse("", "// test */", "");
        tester.testParse("", "// test \"", "");
        tester.testParse("", "// test '", "");
    }
}