package com.baloot.IE_CA_3.Baloot.Exceptions;

public class NegativeCreditAddingException extends Exception {
    public NegativeCreditAddingException() {
        super("Cannot add non-positive value to credit!");
    }
}
