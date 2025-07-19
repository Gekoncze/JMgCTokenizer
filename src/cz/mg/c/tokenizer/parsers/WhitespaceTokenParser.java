package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.token.tokens.WhitespaceToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;

public @Service class WhitespaceTokenParser implements TokenParser {
    private static volatile @Service WhitespaceTokenParser instance;

    public static @Service WhitespaceTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new WhitespaceTokenParser();
                }
            }
        }
        return instance;
    }

    private WhitespaceTokenParser() {
    }

    @Override
    public @Optional WhitespaceToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::space)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            while (reader.has(this::space)) {
                builder.append(reader.read());
            }
            return builder.build(WhitespaceToken::new);
        } else if (reader.has(this::tab) || reader.has(this::newline)) {
            return TokenBuilder.build(reader, WhitespaceToken::new);
        } else {
            return null;
        }
    }

    private boolean space(char ch) {
        return ch == ' ';
    }

    private boolean tab(char ch) {
        return ch == '\t';
    }

    private boolean newline(char ch) {
        return ch == '\n';
    }
}