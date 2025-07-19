package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class HexNumberParser {
    private static volatile @Service HexNumberParser instance;

    public static @Service HexNumberParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new HexNumberParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] HEX = new boolean[128];

    private HexNumberParser() {
        for (char ch = 'A'; ch <= 'F'; ch++) {
            HEX[ch] = true;
        }

        for (char ch = 'a'; ch <= 'f'; ch++) {
            HEX[ch] = true;
        }

        for (char ch = '0'; ch <= '9'; ch++) {
            HEX[ch] = true;
        }
    }

    public int parse(@Mandatory CharacterReader reader) {
        int position = reader.getPosition();

        StringBuilder builder = new StringBuilder();
        while (reader.has(this::hex)) {
            builder.append(reader.read());
        }

        try {
            return Integer.parseInt(builder.toString(), 16);
        } catch (NumberFormatException e) {
            throw new TokenizeException(position, "Could not parse hex number.", e);
        }
    }

    private boolean hex(char ch) {
        return ch < HEX.length && HEX[ch];
    }
}
