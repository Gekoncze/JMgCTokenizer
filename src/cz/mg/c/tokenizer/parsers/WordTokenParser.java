package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.token.tokens.WordToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;

public @Service class WordTokenParser implements TokenParser {
    private static volatile @Service WordTokenParser instance;

    public static @Service WordTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new WordTokenParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] WORD = new boolean[128];
    private final boolean[] WORD_OR_NUMBER = new boolean[128];

    private WordTokenParser() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            WORD[ch] = true;
            WORD_OR_NUMBER[ch] = true;
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            WORD[ch] = true;
            WORD_OR_NUMBER[ch] = true;
        }

        for (char ch = '0'; ch <= '9'; ch++) {
            WORD_OR_NUMBER[ch] = true;
        }

        WORD['_'] = true;
        WORD_OR_NUMBER['_'] = true;
    }

    @Override
    public @Optional WordToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::word)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            while (reader.has(this::wordOrNumber)) {
                builder.append(reader.read());
            }
            return builder.build(WordToken::new);
        } else {
            return null;
        }
    }

    private boolean word(char ch) {
        return ch < WORD.length && WORD[ch];
    }

    private boolean wordOrNumber(char ch) {
        return ch < WORD_OR_NUMBER.length && WORD_OR_NUMBER[ch];
    }
}