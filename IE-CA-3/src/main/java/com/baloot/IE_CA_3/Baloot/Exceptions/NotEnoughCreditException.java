package com.baloot.IE_CA_3.Baloot.Exceptions;

public class NotEnoughCreditException extends Exception {
    public NotEnoughCreditException() {
        super("There is not enough credit in your account to make purchase!");
    }
}
