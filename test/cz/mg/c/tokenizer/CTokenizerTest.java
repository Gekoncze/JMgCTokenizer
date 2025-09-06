package cz.mg.c.tokenizer;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.tokens.NumberToken;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WhitespaceToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.comments.MultiLineCommentToken;
import cz.mg.token.tokens.comments.SingleLineCommentToken;
import cz.mg.token.tokens.quotes.DoubleQuoteToken;

public @Test class CTokenizerTest {
    public static void main(String[] args) {
        System.out.print("Running " + CTokenizerTest.class.getSimpleName() + " ... ");

        CTokenizerTest test = new CTokenizerTest();
        test.testTokenize();

        System.out.println("OK");
    }

    private final @Mandatory CTokenizer tokenizer = new CTokenizer();

    private void testTokenize() {
        testTokenize(
            "int a += 0; // test",
            new WordToken("int", 0),
            new WhitespaceToken(" ", 3),
            new WordToken("a", 4),
            new WhitespaceToken(" ", 5),
            new SymbolToken("+=", 6),
            new WhitespaceToken(" ", 8),
            new NumberToken("0", 9),
            new SymbolToken(";", 10),
            new WhitespaceToken(" ", 11),
            new SingleLineCommentToken(" test", 12)
        );

        testTokenize(
            "char* text=\"a 1 + '\";//comment of the day\naha/*a 1 + '*/hah",
            new WordToken("char", 0),
            new SymbolToken("*", 4),
            new WhitespaceToken(" ", 5),
            new WordToken("text", 6),
            new SymbolToken("=", 10),
            new DoubleQuoteToken("a 1 + '", 11),
            new SymbolToken(";", 20),
            new SingleLineCommentToken("comment of the day", 21),
            new WhitespaceToken("\n", 41),
            new WordToken("aha", 42),
            new MultiLineCommentToken("a 1 + '", 45),
            new WordToken("hah", 56)
        );

        testTokenize(
            "1*(3.14/[0x32])-0.1",
            new NumberToken("1", 0),
            new SymbolToken("*", 1),
            new SymbolToken("(", 2),
            new NumberToken("3.14", 3),
            new SymbolToken("/", 7),
            new SymbolToken("[", 8),
            new NumberToken("0x32", 9),
            new SymbolToken("]", 13),
            new SymbolToken(")", 14),
            new SymbolToken("-", 15),
            new NumberToken("0.1", 16)
        );
    }

    private void testTokenize(@Mandatory String content, Token... expectedTokens) {
        TokenAssert.assertEquals(
            new List<>(expectedTokens),
            tokenizer.tokenize(content)
        );
    }
}