package cz.mg.c.tokenizer.parsers;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.test.Assert;
import cz.mg.tokenizer.components.TokenParser;
import cz.mg.token.Token;
import cz.mg.tokenizer.components.CharacterReader;
import cz.mg.tokenizer.exceptions.TokenizeException;

public @Component class TokenParserTester {
    private final @Mandatory TokenParser parser;
    private final int beforeCount;
    private final int afterCount;
    private final @Mandatory Class<? extends Token> type;

    public TokenParserTester(
        @Mandatory TokenParser parser,
        int beforeCount,
        int afterCount,
        @Mandatory Class<? extends Token> type
    ) {
        this.parser = parser;
        this.beforeCount = beforeCount;
        this.afterCount = afterCount;
        this.type = type;
    }

    public void testException(@Mandatory String content) {
        CharacterReader reader = new CharacterReader(content);
        Assert.assertThatCode(() -> {
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

        int expectedTokenPosition = before.length();
        String expectedTokenText = token.substring(beforeCount, token.length() - afterCount);
        int expectedReaderPosition = (before + token).length();

        for (int i = 0; i < content.length() && i <= expectedTokenPosition; i++) {
            Token actualToken = parser.parse(reader);
            if (i == expectedTokenPosition) {
                Assert.assertNotNull(actualToken);
                Assert.assertThat(expectedTokenText, actualToken.getText())
                    .withPrintFunction(t -> '"' + t + '"')
                    .areEqual();
                Assert.assertEquals(expectedTokenPosition, actualToken.getPosition());
                Assert.assertEquals(expectedReaderPosition, reader.getPosition());
                Assert.assertThat(type, actualToken.getClass())
                    .withPrintFunction(Class::getSimpleName)
                    .areEqual();
                return;
            } else {
                Assert.assertNull(actualToken);
                reader.read();
            }
        }
        throw new IllegalArgumentException("No token found.");
    }
}