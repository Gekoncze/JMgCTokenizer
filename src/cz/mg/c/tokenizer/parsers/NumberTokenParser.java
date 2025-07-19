package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.token.tokens.NumberToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;

public @Service class NumberTokenParser implements TokenParser {
    private static volatile @Service NumberTokenParser instance;

    public static @Service NumberTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new NumberTokenParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] NUMBER = new boolean[128];
    private final boolean[] NUMBER_OR_OTHER = new boolean[128];

    private NumberTokenParser() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            NUMBER_OR_OTHER[ch] = true;
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            NUMBER_OR_OTHER[ch] = true;
        }

        for (char ch = '0'; ch <= '9'; ch++) {
            NUMBER[ch] = true;
            NUMBER_OR_OTHER[ch] = true;
        }

        NUMBER_OR_OTHER['.'] = true;
    }

    @Override
    public @Optional NumberToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::number)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            while (reader.has(this::numberOrOther)) {
                builder.append(reader.read());
            }
            return builder.build(NumberToken::new);
        } else {
            return null;
        }
    }

    private boolean number(char ch) {
        return ch < NUMBER.length && NUMBER[ch];
    }

    private boolean numberOrOther(char ch) {
        return ch < NUMBER_OR_OTHER.length && NUMBER_OR_OTHER[ch];
    }
}