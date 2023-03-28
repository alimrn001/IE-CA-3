package com.baloot.IE_CA_3.Baloot.Exceptions;

public class UserNotExistsException extends Exception {
    public UserNotExistsException() {
        super("The selected user does not exists!");
    }
}
