package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.token.tokens.comments.SingleLineCommentToken;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenBuilder;
import cz.mg.tokenizer.components.TokenParser;

public @Service class SingleLineCommentTokenParser implements TokenParser {
    private static volatile @Service SingleLineCommentTokenParser instance;

    public static @Service SingleLineCommentTokenParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new SingleLineCommentTokenParser();
                }
            }
        }
        return instance;
    }

    private SingleLineCommentTokenParser() {
    }

    @Override
    public @Optional SingleLineCommentToken parse(@Mandatory CharacterReader reader) {
        if (reader.has(this::slash) && reader.hasNext(this::slash)) {
            TokenBuilder builder = new TokenBuilder(reader.getPosition());
            reader.read();
            reader.read();
            while (reader.has() && !reader.has(this::newline)) {
                builder.append(reader.read());
            }
            return builder.build(SingleLineCommentToken::new);
        } else {
            return null;
        }
    }

    private boolean slash(char ch) {
        return ch == '/';
    }

    private boolean newline(char ch) {
        return ch == '\n';
    }
}