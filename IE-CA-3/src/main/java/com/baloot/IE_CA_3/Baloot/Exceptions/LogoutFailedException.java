package com.baloot.IE_CA_3.Baloot.Exceptions;

public class LogoutFailedException extends Exception {
    public LogoutFailedException() {
        super("Failed to logout! No user is logged in!");
    }
}
