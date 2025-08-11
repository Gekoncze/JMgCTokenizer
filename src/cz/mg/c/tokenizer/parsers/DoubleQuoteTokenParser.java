package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.tokenizer.parsers.escape.EscapeSequenceParser;
import cz.mg.token.tokens.quotes.DoubleQuoteToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class DoubleQuoteTokenParser implements TokenParser {
    private static volatile @Service DoubleQuoteTokenParser instance;

    public static @Service DoubleQuoteTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DoubleQuoteTokenParser();
                    instance.escapeSequenceParser = EscapeSequenceParser.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service EscapeSequenceParser escapeSequenceParser;

    private DoubleQuoteTokenParser() {
    }

    @Override
    public @Optional DoubleQuoteToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::doubleQuote)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            reader.read();
            while (reader.has()) {
                if (reader.has(this::backslash)) {
                    builder.append(escapeSequenceParser.parse(reader));
                } else if (reader.has(this::doubleQuote)) {
                    reader.read();
                    return builder.build(DoubleQuoteToken::new);
                } else {
                    builder.append(reader.read());
                }
            }
            throw new TokenizeException(builder.getPosition(), "Unclosed double quotes.");
        } else {
            return null;
        }
    }

    private boolean doubleQuote(char ch) {
        return ch == '"';
    }

    private boolean backslash(char ch) {
        return ch == '\\';
    }
}