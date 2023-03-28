package com.baloot.IE_CA_3.Baloot.Exceptions;

public class LoginFailedException extends Exception {
    public LoginFailedException() {
        super("Failed to login! Wrong username or password!");
    }
}
