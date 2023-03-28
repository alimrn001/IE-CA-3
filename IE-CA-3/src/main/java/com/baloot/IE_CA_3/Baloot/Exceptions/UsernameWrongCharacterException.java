package com.baloot.IE_CA_3.Baloot.Exceptions;

public class UsernameWrongCharacterException extends Exception {
    public UsernameWrongCharacterException() {
        super("Username can not contain characters like # @ ! ....");
    }
}
