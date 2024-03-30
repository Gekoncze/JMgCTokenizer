package cz.mg.c.tokenizer;

import cz.mg.annotations.classes.Component;
import cz.mg.c.tokenizer.parsers.*;
import cz.mg.tokenizer.SimpleTokenizer;

public @Component class CTokenizer extends SimpleTokenizer {
    public CTokenizer() {
        super(
            SingleLineCommentTokenParser.getInstance(),
            MultiLineCommentTokenParser.getInstance(),
            SingleQuoteTokenParser.getInstance(),
            DoubleQuoteTokenParser.getInstance(),
            WhitespaceTokenParser.getInstance(),
            NumberTokenParser.getInstance(),
            WordTokenParser.getInstance(),
            SymbolsTokenParser.getInstance(),
            SymbolTokenParser.getInstance()
        );
    }
}
