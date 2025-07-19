package cz.mg.c.tokenizer.parsers.escape;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Service class UnicodeNumberParser {
    private static volatile @Service UnicodeNumberParser instance;

    public static @Service UnicodeNumberParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new UnicodeNumberParser();
                }
            }
        }
        return instance;
    }

    private final boolean[] HEX = new boolean[128];

    private UnicodeNumberParser() {
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

    public int parseShort(@Mandatory CharacterReader reader) {
        int position = reader.getPosition();

        StringBuilder builder = new StringBuilder();
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));

        try {
            return Integer.parseInt(builder.toString(), 16);
        } catch (NumberFormatException e) {
            throw new TokenizeException(position, "Could not parse unicode number.", e);
        }
    }

    public int parseLong(@Mandatory CharacterReader reader) {
        int position = reader.getPosition();

        StringBuilder builder = new StringBuilder();
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));
        builder.append(reader.read(this::hex));

        try {
            return Integer.parseInt(builder.toString(), 16);
        } catch (NumberFormatException e) {
            throw new TokenizeException(position, "Could not parse unicode number.", e);
        }
    }

    private boolean hex(char ch) {
        return ch < HEX.length && HEX[ch];
    }
}
