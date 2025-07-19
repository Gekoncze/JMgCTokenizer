package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class OctalNumberParser {
    private static volatile @Service OctalNumberParser instance;

    public static @Service OctalNumberParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new OctalNumberParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] OCTAL = new boolean[128];

    private OctalNumberParser() {
        for (char ch = '0'; ch <= '7'; ch++) {
            OCTAL[ch] = true;
        }
    }

    public int parse(@Mandatory CharacterReader reader) {
        int position = reader.getPosition();

        StringBuilder builder = new StringBuilder();
        while (reader.has(this::octal)) {
            builder.append(reader.read());
        }

        try {
            return Integer.parseInt(builder.toString(), 8);
        } catch (NumberFormatException e) {
            throw new TokenizeException(position, "Could not parse octal number.", e);
        }
    }

    public boolean octal(char ch) {
        return ch < OCTAL.length && OCTAL[ch];
    }
}
