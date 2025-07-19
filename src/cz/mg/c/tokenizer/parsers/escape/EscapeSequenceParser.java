package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class EscapeSequenceParser {
    private static volatile @Service EscapeSequenceParser instance;

    public static @Service EscapeSequenceParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new EscapeSequenceParser();
                }
            }
        }
        return instance;
    }

    private EscapeSequenceParser() {
    }

    public char parse(@Mandatory CharacterReader reader) {
        reader.read('\\');
        char ch = reader.read();
        return switch (ch) {
            case ('a') -> 0x07;
            case ('b') -> 0x08;
            case ('e') -> 0x1B;
            case ('f') -> 0x0C;
            case ('n') -> 0x0A;
            case ('r') -> 0x0D;
            case ('t') -> 0x09;
            case ('v') -> 0x0B;
            case ('\\') -> 0x5C;
            case ('\'') -> 0x27;
            case ('"') -> 0x22;
            case ('?') -> 0x3F;
            default -> throw new TokenizeException(
                reader.getPosition(),
                "Unsupported escape sequence \\" + ch + "."
            );
        };
    }
}
