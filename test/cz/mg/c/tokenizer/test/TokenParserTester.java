package cz.mg.c.tokenizer.test;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.test.Assert;
import cz.mg.test.Assertions;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.components.TokenParser;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Component class TokenParserTester {
    private final @Mandatory TokenParser parser;
    private final int beforeCount;
    private final int afterCount;
    private final @Mandatory TokenFactory tokenFactory;

    public TokenParserTester(
        @Mandatory TokenParser parser,
        int beforeCount,
        int afterCount,
        @Mandatory TokenFactory tokenFactory
    ) {
        this.parser = parser;
        this.beforeCount = beforeCount;
        this.afterCount = afterCount;
        this.tokenFactory = tokenFactory;
    }

    public void testException(@Mandatory String content) {
        CharacterReader reader = new CharacterReader(content);
        Assertions.assertThatCode(() -> {
            for (int i = 0; i < content.length(); i++) {
                parser.parse(reader);
                reader.read();
            }
        }).throwsException(TokenizeException.class);
    }

    public void testParse(@Mandatory String content) {
        CharacterReader reader = new CharacterReader(content);
        for (int i = 0; i < content.length(); i++) {
            Token actualToken = parser.parse(reader);
            Assert.assertNull(actualToken);
            reader.read();
        }
    }

    public void testParse(@Mandatory String before, @Mandatory String token, @Mandatory String after) {
        String content = before + token + after;
        CharacterReader reader = new CharacterReader(content);

        Token expectedToken = tokenFactory.create();
        expectedToken.setPosition(before.length());
        expectedToken.setText(token.substring(beforeCount, token.length() - afterCount));

        int expectedReaderPosition = (before + token).length();

        for (int i = 0; i < content.length() && i <= expectedToken.getPosition(); i++) {
            Token actualToken = parser.parse(reader);
            if (i == expectedToken.getPosition()) {
                Assert.assertNotNull(actualToken);
                TokenAssert.assertEquals(expectedToken, actualToken);
                Assert.assertEquals(expectedReaderPosition, reader.getPosition());
                return;
            } else {
                Assert.assertNull(actualToken);
                reader.read();
            }
        }
        throw new IllegalArgumentException("No token found.");
    }
}