package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.tokenizer.components.TokenParser;
import cz.mg.token.tokens.quote.SingleQuoteToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class SingleQuoteTokenParser implements TokenParser {
    private static volatile @Service SingleQuoteTokenParser instance;

    public static @Service SingleQuoteTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new SingleQuoteTokenParser();
                }
            }
        }
        return instance;
    }

    private SingleQuoteTokenParser() {
    }

    @Override
    public @Optional SingleQuoteToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::singleQuote)) {
            return parse(reader, new TokenBuilder(reader.getPosition()));
        } else {
            return null;
        }
    }

    private @Mandatory SingleQuoteToken parse(@Mandatory CharacterReader reader, @Mandatory TokenBuilder builder) {
        reader.read();
        while (reader.has()) {
            if (reader.has(this::backslash) && reader.hasNext()) {
                builder.append(reader.read());
                builder.append(reader.read());
            } else if (reader.has(this::singleQuote)) {
                reader.read();
                return builder.build(SingleQuoteToken::new);
            } else {
                builder.append(reader.read());
            }
        }
        throw new TokenizeException(builder.getPosition(), "Unclosed single quotes.");
    }

    private boolean singleQuote(char ch) {
        return ch == '\'';
    }

    private boolean backslash(char ch) {
        return ch == '\\';
    }
}