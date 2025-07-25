package cz.mg.c.tokenizer;

import cz.mg.annotations.classes.Test;
import cz.mg.c.tokenizer.parsers.*;
import cz.mg.c.tokenizer.parsers.escape.EscapeSequenceParserTest;
import cz.mg.c.tokenizer.parsers.escape.HexNumberParserTest;
import cz.mg.c.tokenizer.parsers.escape.OctalNumberParserTest;
import cz.mg.c.tokenizer.parsers.escape.UnicodeNumberParserTest;

public @Test class AllTests {
    public static void main(String[] args) {
        // cz.mg.c.tokenizer.escape
        EscapeSequenceParserTest.main(args);
        HexNumberParserTest.main(args);
        OctalNumberParserTest.main(args);
        UnicodeNumberParserTest.main(args);

        // cz.mg.c.tokenizer.parsers
        DoubleQuoteTokenParserTest.main(args);
        MultiLineCommentTokenParserTest.main(args);
        NumberTokenParserTest.main(args);
        SingleLineCommentTokenParserTest.main(args);
        SingleQuoteTokenParserTest.main(args);
        SymbolsTokenParserTest.main(args);
        SymbolTokenParserTest.main(args);
        WhitespaceTokenParserTest.main(args);
        WordTokenParserTest.main(args);

        // cz.mg.c.tokenizer
        CTokenizerTest.main(args);
    }
}
