package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;

public @Service class SymbolsTokenParser implements TokenParser {
    private static volatile @Service SymbolsTokenParser instance;

    public static @Service SymbolsTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new SymbolsTokenParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] SYMBOL = new boolean[128];

    private SymbolsTokenParser() {
        SYMBOL['+'] = true;
        SYMBOL['-'] = true;
        SYMBOL['*'] = true;
        SYMBOL['/'] = true;
        SYMBOL['%'] = true;
        SYMBOL['^'] = true;
        SYMBOL['<'] = true;
        SYMBOL['>'] = true;
        SYMBOL['='] = true;
        SYMBOL['~'] = true;
        SYMBOL['!'] = true;
        SYMBOL['&'] = true;
        SYMBOL['|'] = true;
        SYMBOL['?'] = true;
        SYMBOL[':'] = true;
        SYMBOL['.'] = true;
        SYMBOL['#'] = true;
    }

    @Override
    public @Optional SymbolToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::symbol)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            while (reader.has(this::symbol)) {
                builder.append(reader.read());
            }
            return builder.build(SymbolToken::new);
        } else {
            return null;
        }
    }

    private boolean symbol(char ch) {
        return ch < SYMBOL.length && SYMBOL[ch];
    }
}