package cz.mg.c.tokenizer.test;

import cz.mg.annotations.classes.Functional;
import cz.mg.token.Token;

public @Functional interface TokenFactory {
    Token create();
}
